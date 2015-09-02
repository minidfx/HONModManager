package com.honmodmanager.models;

import com.github.jlinqer.collections.Dictionary;
import com.github.jlinqer.collections.List;
import com.honmodmanager.models.contracts.CopyFileElement;
import com.honmodmanager.models.contracts.EditFileElement;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.models.contracts.Requierement;
import com.honmodmanager.models.contracts.Version;
import java.net.URI;
import java.nio.file.Path;
import java.util.Date;
import javafx.scene.image.Image;

public final class ModImpl implements Mod
{
    private final String name;
    private final Version version;
    private boolean isEnabled;
    private final List<String> incompatibillities;
    private final List<String> requiredMods;
    private final Dictionary<String, Version> applyAfter;
    private final Dictionary<String, Version> applyBefore;

    private Image icon;
    private Date date;
    public String description;
    private String author;
    private URI webLink;
    private URI versionAddress;
    private URI downloadAddress;
    private Version modVersion;
    private Requierement gameVersion;
    private String modId;
    private Path filePath;
    private List<CopyFileElement> copyElements;
    private List<EditFileElement> editElements;

    public ModImpl(String name, Version version, boolean isEnabled)
    {
        this.name = name;
        this.version = version;
        this.isEnabled = isEnabled;

        this.incompatibillities = new List<>();
        this.requiredMods = new List<>();
        this.applyAfter = new Dictionary<>();
        this.applyBefore = new Dictionary<>();
        this.copyElements = new List<>();
        this.editElements = new List<>();
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public Version getVersion()
    {
        return this.version;
    }

    @Override
    public String getId()
    {
        return this.modId;
    }

    @Override
    public boolean isEnabled()
    {
        return this.isEnabled;
    }

    @Override
    public Image getIcon()
    {
        return this.icon;
    }

    @Override
    public void setIcon(Image value)
    {
        this.icon = value;
    }

    @Override
    public Date getDate()
    {
        return this.date;
    }

    @Override
    public void setDate(Date value)
    {
        this.date = value;
    }

    @Override
    public void setAuthor(String value)
    {
        this.author = value;
    }

    @Override
    public String getAuthor()
    {
        return this.author;
    }

    @Override
    public URI getWebLink()
    {
        return this.webLink;
    }

    @Override
    public void setWebLink(URI value)
    {
        this.webLink = value;
    }

    @Override
    public void setVersionAddress(URI uri)
    {
        this.versionAddress = uri;
    }

    @Override
    public URI getVersionAddress()
    {
        return this.versionAddress;
    }

    @Override
    public URI getDownloadAddress()
    {
        return this.downloadAddress;
    }

    @Override
    public void setDownloadAddress(URI value)
    {
        this.downloadAddress = value;
    }

    @Override
    public Version getModManagerVersion()
    {
        return this.modVersion;
    }

    @Override
    public void setModManagerVersion(Version value)
    {
        this.modVersion = value;
    }

    @Override
    public Requierement getGameVersion()
    {
        return this.gameVersion;
    }

    @Override
    public void setGameVersion(Requierement value)
    {
        this.gameVersion = value;
    }

    @Override
    public void setId(String value)
    {
        this.modId = value;
    }

    @Override
    public List<String> getIncompatibillities()
    {
        return this.incompatibillities;
    }

    @Override
    public void addIncompatibillity(String modId)
    {
        this.incompatibillities.add(modId);
    }

    @Override
    public List<String> getRequiredMods()
    {
        return this.requiredMods;
    }

    @Override
    public void addRequirement(String modId)
    {
        this.requiredMods.add(modId);
    }

    @Override
    public Dictionary<String, Version> getApplyAfter()
    {
        return this.applyAfter;
    }

    @Override
    public void addApplyAfter(String modId, Version version)
    {
        this.applyAfter.put(modId, version);
    }

    @Override
    public Dictionary<String, Version> getApplyBefore()
    {
        return this.applyBefore;
    }

    @Override
    public void addApplyBefore(String modId, Version version)
    {
        this.applyBefore.put(modId, version);
    }

    @Override
    public void enabled(boolean value)
    {
        this.isEnabled = value;
    }

    @Override
    public Path getFilePath()
    {
        return this.filePath;
    }

    @Override
    public void setFilePath(Path value)
    {
        this.filePath = value;
    }

    @Override
    public void addCopyElement(CopyFileElement value)
    {
        this.copyElements.add(value);
    }

    @Override
    public List<CopyFileElement> getCopyElements()
    {
        return this.copyElements;
    }

    @Override
    public void addEditElement(EditFileElement value)
    {
        this.editElements.add(value);
    }

    @Override
    public List<EditFileElement> getEditElements()
    {
        return this.editElements;
    }
}
