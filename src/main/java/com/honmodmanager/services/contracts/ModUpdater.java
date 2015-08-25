package com.honmodmanager.services.contracts;

import com.honmodmanager.models.contracts.Mod;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import javafx.util.Pair;
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
     * @throws java.net.MalformedURLException
     * @throws java.text.ParseException
     */
    Observable<Pair<Mod, File>> Update(Mod mod) throws MalformedURLException,
                                                       IOException,
                                                       ParseException;
}
