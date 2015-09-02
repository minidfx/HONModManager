package com.honmodmanager.services;

import com.honmodmanager.exceptions.ModUpdateException;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.models.contracts.Version;
import com.honmodmanager.services.contracts.ModDownloader;
import com.honmodmanager.services.contracts.ModUpdater;
import com.honmodmanager.services.contracts.VersionParser;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.Subscriber;

/**
 * Implementation for updating mods of Heroes of Newerth.
 *
 * @author Burgy Benjamin
 * @see http://stackoverflow.com/a/921400
 */
@Service
@Scope("prototype")
public final class ModUpdaterImpl implements ModUpdater
{
    private final ModDownloader downloader;
    private final VersionParser versionParser;
    private static final Logger LOG = Logger.getLogger(ModUpdaterImpl.class.getName());

    @Autowired
    public ModUpdaterImpl(ModDownloader downloader, VersionParser versionParser)
    {
        this.downloader = downloader;
        this.versionParser = versionParser;
    }

    @Override
    public Observable<Pair<Mod, File>> Update(Mod mod) throws IOException,
                                                              MalformedURLException,
                                                              ParseException
    {
        return Observable.create(subscriber ->
        {
            try
            {
                File modFile = mod.getFilePath().toFile();
                String modName = mod.getName();

                LOG.info(String.format("Searching update for the mod %s ...", modName));

                Version availableVersion = this.getModVersion(mod);
                Version modVersion = mod.getVersion();

                if (availableVersion.greaterThan(modVersion))
                {
                    LOG.info(String.format("Updating the mod %s ...", modName));

                    this.downloader.download(mod.getDownloadAddress(), modFile);

                    LOG.info(String.format("Mod %s udpated successfully.", modName));
                }
                else
                {
                    LOG.info(String.format("No update found for %s.", modName));
                }

                subscriber.onNext(new Pair<>(mod, modFile));
                subscriber.onCompleted();
            }
            catch (Exception ex)
            {
                subscriber.onError(new ModUpdateException(mod, ex));
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        });
    }

    private Version getModVersion(Mod mod) throws MalformedURLException,
                                                  IOException,
                                                  ParseException
    {
        byte[] bytes = this.downloader.download(mod.getVersionAddress().toURL());
        String version = new String(bytes);

        return this.versionParser.parse(version);
    }
}
