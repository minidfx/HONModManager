package com.honmodmanager.services.contracts;

import com.honmodmanager.models.contracts.Mod;
import java.io.File;
import rx.Observable;

/**
 * Enables the class to update mods installed on Heroes of Newerth.
 *
 * @author Burgy Benjamin
 */
public interface ModUpdater
{
    /**
     * Updates mods installed in the game.
     *
     * @param mod The mod that will be updated.
     * @return An observable of the file download.
     */
    Observable<File> Update(Mod mod);
}
