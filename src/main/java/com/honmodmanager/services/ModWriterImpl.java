package com.honmodmanager.services;

import com.github.jlinqer.collections.Dictionary;
import com.github.jlinqer.collections.List;
import com.github.jlinqer.linq.IEnumerable;
import com.honmodmanager.exceptions.ApplyModException;
import com.honmodmanager.models.contracts.CopyFileElement;
import com.honmodmanager.models.contracts.EditFileElement;
import com.honmodmanager.models.contracts.EditOperation;
import com.honmodmanager.models.contracts.EditOperationType;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.services.contracts.ConditionEvaluator;
import com.honmodmanager.services.contracts.GameInformation;
import com.honmodmanager.services.contracts.ModReader;
import com.honmodmanager.services.contracts.ModSorter;
import com.honmodmanager.services.contracts.ModWriter;
import com.honmodmanager.services.contracts.ZipCommentsBuilder;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.Subscriber;

@Service
@Scope("singleton")
public final class ModWriterImpl implements ModWriter
{
    private static final Logger LOG = Logger.getLogger(ModWriterImpl.class.getName());

    private final GameInformation gameInformation;
    private final ModReader modReader;
    private final ModSorter modSorter;
    private final ZipCommentsBuilder zipCommentsBuilder;
    private final ConditionEvaluator conditionEvaluator;

    @Autowired
    public ModWriterImpl(GameInformation gameInformation,
                         ModReader modReader,
                         ZipCommentsBuilder zipCommentsBuilder,
                         ConditionEvaluator conditionEvaluator,
                         ModSorter modSorter)
    {
        this.gameInformation = gameInformation;
        this.modReader = modReader;
        this.zipCommentsBuilder = zipCommentsBuilder;
        this.conditionEvaluator = conditionEvaluator;
        this.modSorter = modSorter;
    }

    private void backupResources()
    {
        File resources = this.gameInformation.getAdditonalResourcePath().toFile();
        File backupResources = getBackupResource(resources);

        if (backupResources.exists())
        {
            backupResources.delete();
        }

        if (resources.exists())
        {
            resources.renameTo(backupResources);
        }
    }

    private File getBackupResource(File resources)
    {
        return new File(String.format("%s.bak", resources.toString()));
    }

    private void restoreBackup()
    {
        File resources = this.gameInformation.getAdditonalResourcePath().toFile();
        File backupResources = getBackupResource(resources);

        if (backupResources.exists())
        {
            backupResources.renameTo(resources);
        }
    }

    private void removeBackup()
    {
        File resources = this.gameInformation.getAdditonalResourcePath().toFile();
        File backupResources = getBackupResource(resources);

        if (backupResources.exists())
        {
            backupResources.delete();
        }
    }

    @Override
    public Observable<Boolean> Write()
    {
        return Observable.create((Subscriber<? super Boolean> subscriber) ->
        {
            LOG.info("Applying mods ...");
            this.backupResources();

            try
            {
                List<Mod> mods = this.modReader.getCachedMods();

                IEnumerable<Mod> enabledMods = mods.where(x -> x.isEnabled());

                if (!enabledMods.any())
                {
                    throw new ApplyModException("No mod enabled found to be applied.");
                }

                IEnumerable<Mod> sortedMods = this.modSorter.sort(enabledMods);
                Dictionary<String, byte[]> filesBytes = new Dictionary<>();

                try
                {
                    this.zipCommentsBuilder.init();

                    try (ZipFile originalResource = new ZipFile(this.gameInformation.getOriginalResourcePath().toFile()))
                    {
                        for (Mod mod : sortedMods)
                        {
                            try (ZipFile zipMod = new ZipFile(mod.getFilePath().toFile()))
                            {
                                this.zipCommentsBuilder.addMod(mod);

                                Dictionary<String, byte[]> files = this.copyFiles(originalResource, zipMod, mod);
                                
                                for(Entry<String, byte[]> file : files)
                                {
                                    filesBytes.put(file.getKey(), file.getValue());
                                }
                            }
                        }

                        for (Mod mod : sortedMods)
                        {
                            this.editFiles(mod, filesBytes);
                        }
                    }

                    try (ZipOutputStream additionalResource = new ZipOutputStream(new FileOutputStream(this.gameInformation.getAdditonalResourcePath().toFile())))
                    {
                        for (Entry<String, byte[]> fileBytes : filesBytes)
                        {
                            this.AddZipEntry(additionalResource, fileBytes.getKey(), fileBytes.getValue());
                        }

                        additionalResource.setComment(this.zipCommentsBuilder.build());
                    }

                    this.removeBackup();

                    subscriber.onNext(true);
                    subscriber.onCompleted();

                    LOG.info("Mods applied.");
                }
                finally
                {
                    filesBytes = null;
                }
            }
            catch (Exception ex)
            {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);

                this.restoreBackup();
                subscriber.onError(ex);
            }
        });
    }

    private void AddZipEntry(ZipOutputStream zipInputStream, String path, byte[] bytes) throws IOException
    {
        ZipEntry newZipEntry = new ZipEntry(path);
        newZipEntry.setCompressedSize(9);

        try
        {
            zipInputStream.putNextEntry(newZipEntry);
            zipInputStream.write(bytes);
        }
        finally
        {
            zipInputStream.closeEntry();
        }
    }

    private void editFiles(Mod mod, Dictionary<String, byte[]> filesBytes) throws IOException, ApplyModException
    {
        for (EditFileElement editFileElement : mod.getEditElements())
        {
            if (this.conditionEvaluator.evaluate(editFileElement.getCondition()))
            {
                final String zipEntryPath = editFileElement.getPath();
                final Entry<String, byte[]> fileBytes = filesBytes.single(x
                        -> x.getKey().equals(zipEntryPath));
                final byte[] zipEntryBytes = fileBytes.getValue();

                byte[] bytes = this.applyEditOperations(editFileElement, zipEntryBytes);

                filesBytes.remove(fileBytes);
                filesBytes.put(zipEntryPath, bytes);

                LOG.info(String.format("Entry path %s edited.", zipEntryPath));
            }
        }
    }

    private Dictionary<String, byte[]> copyFiles(ZipFile originalResource, ZipFile zipMod, Mod mod) throws IOException
    {
        Dictionary<String, byte[]> filesBytes = new Dictionary<>();

        for (CopyFileElement copyFileElement : mod.getCopyElements())
        {
            if (this.conditionEvaluator.evaluate(copyFileElement.getCondition()))
            {
                final String zipEntryPath = copyFileElement.getPath();
                if (filesBytes.any(x -> x.getKey().equals(zipEntryPath)) && copyFileElement.overwrite())
                {
                    filesBytes.remove(filesBytes.single(x
                            -> x.getKey().equals(zipEntryPath)));
                }

                byte[] zipEntryBytes = this.getZipEntry(zipMod, zipEntryPath);
                filesBytes.put(zipEntryPath, zipEntryBytes);

                LOG.info(String.format("Entry path %s copied into the additional resources zip.", zipEntryPath));
            }
        }

        for (EditFileElement editFileElement : mod.getEditElements())
        {
            if (this.conditionEvaluator.evaluate(editFileElement.getCondition()))
            {
                final String zipEntryPath = editFileElement.getPath();
                byte[] zipEntryBytes = this.getZipEntry(originalResource, zipEntryPath);
                if (!filesBytes.any(x -> x.getKey().equals(zipEntryPath)))
                {
                    filesBytes.put(zipEntryPath, zipEntryBytes);

                    LOG.info(String.format("Entry path %s copied into the additional resources zip.", zipEntryPath));
                }
            }
        }

        return filesBytes;
    }

    private void AddReminder()
    {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    private ZipFile getOriginalResource() throws FileNotFoundException, IOException
    {
        File originalResource = this.gameInformation.getOriginalResourcePath().toFile();
        if (!originalResource.exists())
        {
            throw new FileNotFoundException("Original resource not found");
        }

        return new ZipFile(originalResource);
    }

    private byte[] getZipEntry(ZipFile zipFile, String entryPath) throws ZipException, IOException
    {
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream())
        {
            ZipEntry zipEntry = zipFile.getEntry(entryPath);
            if (zipEntry == null)
            {
                throw new ZipException(String.format("Entry %s not found.", entryPath));
            }

            try (InputStream zipEntryStream = zipFile.getInputStream(zipEntry))
            {
                byte[] buffer = new byte[4096];
                int read = 0;

                while ((read = zipEntryStream.read(buffer)) > 0)
                {
                    bytes.write(buffer);
                }

                return bytes.toByteArray();
            }
        }
    }

    private byte[] applyEditOperations(EditFileElement editFileElement, byte[] entryBytes) throws ApplyModException
    {
        String entry = new String(entryBytes);
        Integer lastPositionFound = null;
        Integer findElementLength = null;

        for (EditOperation operation : editFileElement.getOperations())
        {
            EditOperationType operationType = operation.getOperationType();

            switch (operationType)
            {
                case find:
                case search:
                case seek:
                    lastPositionFound = entry.indexOf(operation.getText());
                    findElementLength = operation.getText().length();
                    break;

                case insert:
                    entry = this.applyInsertOperation(entry, lastPositionFound, findElementLength, operation);

                    lastPositionFound = null;
                    findElementLength = null;
                    break;

                case delete:
                    entry = this.applyDeleteOperation(entry, lastPositionFound, findElementLength);

                    lastPositionFound = null;
                    findElementLength = null;
                    break;

                default:
                    throw new ApplyModException(String.format("Operation %s not implemented yet.", operationType));
            }
        }

        return entry.getBytes();
    }

    private String applyDeleteOperation(String entry, Integer lastPositionFound, Integer findElementLength) throws ApplyModException
    {
        if (lastPositionFound == null || findElementLength == null)
        {
            throw new ApplyModException("A search operation is mandatory to delete a text.");
        }

        String toRemove = this.remove(entry, lastPositionFound, lastPositionFound + findElementLength);
        return toRemove;
    }

    private String applyInsertOperation(String entry,
                                        Integer lastPositionFound,
                                        Integer findElementLength,
                                        EditOperation operation) throws ApplyModException, IllegalArgumentException, UnsupportedOperationException
    {
        if (lastPositionFound == null || findElementLength == null)
        {
            throw new ApplyModException("A search operation is mandatory to insert a text.");
        }

        String insertPosition = operation.getAttributes().single(x -> x.getKey().equals("position")).getValue();
        if (insertPosition.equals("after"))
        {
            lastPositionFound += findElementLength;
        }

        String insert = operation.getText();
        entry = this.insert(entry, insert, lastPositionFound);

        return entry;
    }

    private String remove(String base, int startPositin, int endPosition)
    {
        String firstPart = StringUtils.substring(base, 0, startPositin);
        String secondPart = StringUtils.substring(base, endPosition);

        return StringUtils.join(new String[]
        {
            firstPart, secondPart
        });
    }

    private String insert(String base, String insert, int position)
    {
        String firstPart = StringUtils.substring(base, 0, position);
        String secondPart = StringUtils.substring(base, position);

        return StringUtils.join(new String[]
        {
            firstPart, insert, secondPart
        });
    }
}
