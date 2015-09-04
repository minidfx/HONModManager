package com.honmodmanager.services.contracts;

import com.honmodmanager.models.contracts.Mod;
import java.io.IOException;
import java.nio.file.Path;
import rx.Observable;

/**
 * Enables the class to manage mods installed on Heroes of Newerth.
 *
 * @author Burgy Benjamin
 */
public interface ModManager
{
    Observable<Boolean> apply();

    void add(Path path) throws IOException;

    Observable<Mod> getAll();

    void remove(Mod mod);
}
