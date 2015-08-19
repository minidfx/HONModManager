package com.honmodmanager.services;

import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.services.contracts.ModDownloader;
import com.honmodmanager.services.contracts.ModUpdater;
import java.io.File;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import rx.Observable;

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

    public ModUpdaterImpl(ModDownloader downloader)
    {
        this.downloader = downloader;
    }

    @Override
    public Observable<File> Update(Mod mod)
    {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
