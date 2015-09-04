package com.honmodmanager.services;

import com.github.jlinqer.collections.List;
import com.github.jlinqer.linq.IEnumerable;
import com.honmodmanager.exceptions.ApplyModException;
import com.honmodmanager.models.contracts.CopyFileElement;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.services.contracts.ConditionEvaluator;
import com.honmodmanager.services.contracts.GameInformation;
import com.honmodmanager.services.contracts.ModReader;
import com.honmodmanager.services.contracts.ModWriter;
import com.honmodmanager.services.contracts.ZipCommentsBuilder;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javafx.util.Pair;
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
    private final ZipCommentsBuilder zipCommentsBuilder;
    private final ConditionEvaluator conditionEvaluator;

    @Autowired
    public ModWriterImpl(GameInformation gameInformation,
                         ModReader modReader,
                         ZipCommentsBuilder zipCommentsBuilder,
                         ConditionEvaluator conditionEvaluator)
    {
        this.gameInformation = gameInformation;
        this.modReader = modReader;
        this.zipCommentsBuilder = zipCommentsBuilder;
        this.conditionEvaluator = conditionEvaluator;
    }

    private void backupResources()
    {
        File resources = this.gameInformation.getAdditonalResourcePath().toFile();
        File backupResources = new File(String.format("%s.bak", resources.toString()));

        if (backupResources.exists())
            backupResources.delete();

        if (resources.exists())
            resources.renameTo(backupResources);
    }

    private void restoreBackup()
    {
        File resources = this.gameInformation.getAdditonalResourcePath().toFile();
        File backupResources = new File(String.format("%s.bak", resources.toString()));

        if (backupResources.exists())
            backupResources.renameTo(resources);
    }

    @Override
    public Observable<Boolean> Write()
    {
        return Observable.create((Subscriber<? super Boolean> subscriber) ->
        {
            this.backupResources();

            try
            {
                List<Mod> mods = this.modReader.getCachedMods();

                IEnumerable<Mod> enabledMods = mods.where(x -> x.isEnabled());

                if (!enabledMods.any())
                    throw new ApplyModException("No mod enabled found to be applied.");

                IEnumerable<Mod> sortedMods = enabledMods.orderBy(x ->
                {
                    return x.getApplyAfter()
                            .equals(x.getId());
                });

                List<Pair<String, byte[]>> filesBytes = new List<>();

                try
                {
                    this.zipCommentsBuilder.init();

                    for (Mod mod : sortedMods)
                    {
                        this.zipCommentsBuilder.addMod(mod);

                        try (ZipFile zipMod = new ZipFile(mod.getFilePath().toFile()))
                        {
                            List<Pair<String, byte[]>> copyFilesBytes = this.copyFiles(mod, zipMod);
                            filesBytes.addAll(copyFilesBytes);
                        }
                    }

                    try (ZipOutputStream additionalResource = new ZipOutputStream(new FileOutputStream(this.gameInformation.getAdditonalResourcePath().toFile())))
                    {
                        for (Pair<String, byte[]> fileBytes : filesBytes)
                        {
                            this.AddZipEntry(additionalResource, fileBytes.getKey(), fileBytes.getValue());
                        }

                        additionalResource.setComment(this.zipCommentsBuilder.build());
                    }

                    subscriber.onNext(true);
                    subscriber.onCompleted();
                }
                finally
                {
                    filesBytes.clear();
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

    private List<Pair<String, byte[]>> editFiles(Mod mod, ZipFile zipMod) throws IOException
    {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    private List<Pair<String, byte[]>> copyFiles(Mod mod, ZipFile zipMod) throws IOException
    {
        List<Pair<String, byte[]>> filesBytes = new List<>();

        for (CopyFileElement copyFileElement : mod.getCopyElements())
        {
            if (this.conditionEvaluator.evaluate(copyFileElement.getCondition()))
            {
                final String zipEntryPath = copyFileElement.getPath();
                if (filesBytes.any(x -> x.getKey().equals(zipEntryPath)) && copyFileElement.overwrite())
                {
                    filesBytes.remove(filesBytes.single(x ->
                            x.getKey().equals(zipEntryPath)));
                }

                byte[] zipEntryBytes = this.getZipEntry(zipMod, zipEntryPath);
                filesBytes.add(new Pair<>(zipEntryPath, zipEntryBytes));
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
                throw new ZipException(String.format("Entry %s not found.", entryPath));

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
}
