package com.honmodmanager.services;

import com.honmodmanager.exceptions.MalformedModException;
import com.github.jlinqer.collections.List;
import com.github.jlinqer.linq.IEnumerable;
import com.honmodmanager.events.ModEnableDisableEvent;
import com.honmodmanager.exceptions.MalformedZipCommentsException;
import com.honmodmanager.exceptions.ModsFolderNotFoundException;
import com.honmodmanager.models.CopyFileElementImpl;
import com.honmodmanager.models.EditFileElementImpl;
import com.honmodmanager.models.ModImpl;
import com.honmodmanager.models.ModRequirementImpl;
import com.honmodmanager.models.contracts.EditOperation;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.models.contracts.Requirement;
import com.honmodmanager.models.contracts.Version;
import com.honmodmanager.services.contracts.EventAggregator;
import com.honmodmanager.services.contracts.GameInformation;
import com.honmodmanager.services.contracts.ModIdBuilder;
import com.honmodmanager.services.contracts.ModReader;
import com.honmodmanager.services.contracts.RequirementParser;
import com.honmodmanager.services.contracts.VersionParser;
import com.joestelmach.natty.Parser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import javafx.scene.image.Image;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

@Service
@Scope("singleton")
public final class ModReaderImpl implements ModReader
{
    private static final Logger LOG = Logger.getLogger(ModReaderImpl.class.getName());
    private static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    private final GameInformation gameInformation;
    private final VersionParser versionParser;
    private final Parser dateParser;
    private final RequirementParser requierementParser;
    private final ModIdBuilder modIdBuilder;
    private final EventAggregator eventAggregator;

    private List<Mod> cachedMods;

    @Autowired
    public ModReaderImpl(GameInformation gameInformation,
                         VersionParser versionParser,
                         RequirementParser requirementParser,
                         ModIdBuilder modIdBuilder,
                         EventAggregator eventAggregator)
    {
        this.gameInformation = gameInformation;
        this.versionParser = versionParser;

        this.dateParser = new Parser();
        this.requierementParser = requirementParser;
        this.modIdBuilder = modIdBuilder;

        this.cachedMods = new List<>();
        this.eventAggregator = eventAggregator;
    }

    @Override
    public Observable<Mod> getMods()
    {
        this.cachedMods.clear();
        this.cachedMods = new List<>();

        return Observable.create((Subscriber<? super Mod> subscriber) ->
        {
            try
            {
                this.readModFolder(subscriber);
                this.readAdditionalResources();

                subscriber.onCompleted();
            }
            catch (Exception e)
            {
                subscriber.onError(e);
            }
        })
                .subscribeOn(Schedulers.io())
                .doOnNext(x -> this.cachedMods.add(x));
    }

    private void readAdditionalResources() throws ZipException,
                                                  MalformedZipCommentsException,
                                                  IOException,
                                                  ParseException,
                                                  ModsFolderNotFoundException,
                                                  MalformedModException,
                                                  ParserConfigurationException,
                                                  SAXException
    {
        File additionalResourceFile = this.gameInformation.getAdditonalResourcePath().toFile();

        if (additionalResourceFile.exists())
        {
            String zipComments = this.gameInformation.getZipComments(additionalResourceFile);

            if (!zipComments.startsWith("HoN Mod Manager v"))
                throw new MalformedZipCommentsException();

            IEnumerable<String> linesComments = new List<>(zipComments.split("\n"))
                    .skip(4);

            for (String line : linesComments)
            {
                Mod modInstalled = this.initMod(line);

                Mod mod = this.cachedMods.singleOrDefault(x ->
                        x.getId().equals(modInstalled.getId())
                        && x.getVersion().isSame(modInstalled.getVersion()));

                if (mod != null)
                {
                    mod.enabled(true);
                    LOG.info(String.format("Mod installed: %s (%s)", modInstalled.getName(), modInstalled.getVersion()));

                    this.eventAggregator.publish(new ModEnableDisableEvent(mod));
                }
            }
        }
    }

    private void readModFolder(Subscriber<? super Mod> subscriber) throws ModsFolderNotFoundException,
                                                                          IOException,
                                                                          ParserConfigurationException,
                                                                          SAXException
    {
        File modsFolder = this.gameInformation.getModsFolder().toFile();

        if (!modsFolder.exists())
            throw new ModsFolderNotFoundException();

        if (!modsFolder.isDirectory())
            throw new UnsupportedOperationException("Mods of the game must be contained in a folder called mods.");

        File[] mods = modsFolder.listFiles((File file) ->
        {
            return FilenameUtils.getExtension(file.getName()).equals("honmod");
        });

        for (File modFile : mods)
        {
            try
            {
                Mod mod = this.readModFile(modFile);
                mod.setFilePath(Paths.get(modFile.getAbsolutePath()));

                subscriber.onNext(mod);
            }
            catch (MalformedModException | URISyntaxException | ParseException e)
            {
                LOG.log(Level.WARNING, e.getMessage(), e);
            }
        }
    }

    private Mod readModFile(File fileMod) throws IOException,
                                                 MalformedModException,
                                                 ParserConfigurationException,
                                                 SAXException,
                                                 ParseException,
                                                 URISyntaxException
    {
        try (ZipFile zip = new ZipFile(fileMod))
        {
            Enumeration<? extends ZipEntry> entries = zip.entries();

            Image modIcon = null;
            Mod mod = null;

            while (entries.hasMoreElements()
                   && (mod == null
                       || modIcon == null))
            {
                ZipEntry entry = entries.nextElement();
                String entryName = entry.getName();

                if (entryName.equals("icon.png"))
                    modIcon = this.readImage(zip, entry);

                if (entryName.equals("mod.xml"))
                    mod = this.readXml(zip, entry);
            }

            if (mod == null)
                throw new MalformedModException(String.format("File mod.xml not found in the archive %s.", fileMod.getName()));

            if (modIcon != null)
                mod.setIcon(modIcon);
            else
                mod.setIcon(new Image("/images/defaultModIcon.jpg"));

            return mod;
        }
    }

    private Image readImage(ZipFile zip, ZipEntry entry) throws IOException
    {
        try (InputStream zipInputStream = zip.getInputStream(entry))
        {
            return new Image(zipInputStream);
        }
    }

    private Mod initMod(String line) throws ParseException
    {
        Pattern parseModName = Pattern.compile("^(.+).*\\(v(.+)\\)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = parseModName.matcher(line.replace("\r", "").replace("\n", ""));

        if (!matcher.matches())
            throw new ParseException("Cannot parse the mod installed.", 0);

        String name = matcher.group(1);
        String id = this.modIdBuilder.build(name);
        Version version = this.versionParser.parse(matcher.group(2));

        Mod newMod = new ModImpl(line, version, true);
        newMod.setId(id);

        return newMod;
    }

    private Mod readXml(ZipFile zip, ZipEntry entry) throws ParserConfigurationException,
                                                            SAXException,
                                                            IOException,
                                                            MalformedModException,
                                                            ParseException,
                                                            URISyntaxException
    {
        DocumentBuilder documentBuiler = documentBuilderFactory.newDocumentBuilder();
        Document xml = documentBuiler.parse(zip.getInputStream(entry));

        NodeList nodes = xml.getElementsByTagName("modification");

        if (nodes.getLength() < 1)
            throw new MalformedModException("Element modification not found in the xml.");

        Element modificationElement = (Element) nodes.item(0);

        String modName = modificationElement.getAttribute("name");
        String modId = this.modIdBuilder.build(modName);

        Requirement appVersion = this.requierementParser.parse(modificationElement.getAttribute("appversion"));
        Version mmVersion = this.versionParser.parse(modificationElement.getAttribute("mmversion"));
        Version modVersion = this.versionParser.parse(modificationElement.getAttribute("version"));

        String xmlModDate = modificationElement.getAttribute("date");
        Date modDate = null;

        if (!xmlModDate.isEmpty())
            modDate = this.dateParser.parse(xmlModDate).get(0).getDates().get(0);

        String modDescription = modificationElement.getAttribute("description");
        String modAuthor = modificationElement.getAttribute("author");
        URI modWebLink = new URI(modificationElement.getAttribute("weblink"));
        URI versionAddress = new URI(modificationElement.getAttribute("updatecheckurl"));
        URI downloadAddress = new URI(modificationElement.getAttribute("updatedownloadurl"));

        Mod mod = new ModImpl(modName, modVersion, false);

        if (modDate != null)
            mod.setDate(modDate);

        mod.setId(modId);
        mod.setDescription(modDescription);
        mod.setAuthor(modAuthor);
        mod.setWebLink(modWebLink);
        mod.setVersionAddress(versionAddress);
        mod.setDownloadAddress(downloadAddress);
        mod.setModManagerVersion(mmVersion);
        mod.setGameVersion(appVersion);

        readIncompatibillitiesElements(xml, mod);
        readRequirementElements(xml, mod);
        readApplyBeforeElements(xml, mod);
        readApplyAfterElements(xml, mod);
        readCopyFileElements(xml, mod);
        readEditFileElements(xml, mod);

        return mod;
    }

    private Element getFirstChildElement(Element element)
    {
        NodeList nodes = element.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++)
        {
            Node node = nodes.item(i);
            if (node instanceof Element)
            {
                return (Element) node;
            }
        }

        throw new UnsupportedOperationException("Cannot found the first element of an EditFileElement.");
    }

    private void readEditFileElements(Document xml, Mod mod)
    {
        NodeList editFiles = xml.getElementsByTagName("editfile");
        for (int m = 0; m < editFiles.getLength(); m++)
        {
            Element element = (Element) editFiles.item(m);
            String path = element.getAttribute("name");
            String condition = element.getAttribute("condition");

            Element firstElement = this.getFirstChildElement(element);
            EditOperation editOperation = EditOperation.valueOf(firstElement.getNodeName());

            List<Node> operands = new List<>();

            for (int n = 1; n < element.getChildNodes().getLength(); n++)
            {
                Node node = element.getChildNodes().item(n);
                operands.add(node);
            }

            mod.addEditElement(new EditFileElementImpl(path, editOperation, operands, condition));
        }
    }

    private void readCopyFileElements(Document xml, Mod mod)
    {
        NodeList copyFiles = xml.getElementsByTagName("copyfile");
        for (int m = 0; m < copyFiles.getLength(); m++)
        {
            Element element = (Element) copyFiles.item(m);
            String path = element.getAttribute("name");
            String condition = element.getAttribute("condition");
            boolean overwrite = Boolean.valueOf(element.getAttribute("overwrite"));

            mod.addCopyElement(new CopyFileElementImpl(path, overwrite, condition));
        }
    }

    private void readApplyAfterElements(Document xml, Mod mod) throws ParseException
    {
        NodeList applyAfter = xml.getElementsByTagName("applyafter");
        for (int l = 0; l < applyAfter.getLength(); l++)
        {
            Element element = (Element) applyAfter.item(l);
            String applyAfterModId = this.modIdBuilder.build(element.getAttribute("name"));
            Version applyAfterVersion = this.versionParser.parse(element.getAttribute("version"));

            mod.addApplyAfter(applyAfterModId, applyAfterVersion);
        }
    }

    private void readApplyBeforeElements(Document xml, Mod mod) throws ParseException
    {
        NodeList applyBefore = xml.getElementsByTagName("applybefore");
        for (int k = 0; k < applyBefore.getLength(); k++)
        {
            Element element = (Element) applyBefore.item(k);
            String applyBeforeModId = this.modIdBuilder.build(element.getAttribute("name"));
            Version applyBeforeVersion = this.versionParser.parse(element.getAttribute("version"));

            mod.addApplyBefore(applyBeforeModId, applyBeforeVersion);
        }
    }

    private void readRequirementElements(Document xml, Mod mod) throws ParseException
    {
        NodeList requirements = xml.getElementsByTagName("requirement");
        for (int j = 0; j < requirements.getLength(); j++)
        {
            Element element = (Element) requirements.item(j);
            String requiredModId = this.modIdBuilder.build(element.getAttribute("name"));
            Requirement requirementVersions = this.requierementParser.parse(element.getAttribute("version"));

            mod.addRequirement(new ModRequirementImpl(requiredModId, requirementVersions));
        }
    }

    private void readIncompatibillitiesElements(Document xml, Mod mod)
    {
        NodeList incompatibilities = xml.getElementsByTagName("incompatibility");
        for (int i = 0; i < incompatibilities.getLength(); i++)
        {
            Element element = (Element) incompatibilities.item(i);
            String incompatibleModId = this.modIdBuilder.build(element.getAttribute("name"));

            mod.addIncompatibillity(incompatibleModId);
        }
    }

    @Override
    public List<Mod> getCachedMods()
    {
        return this.cachedMods;
    }
}
