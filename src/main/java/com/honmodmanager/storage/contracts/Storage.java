package com.honmodmanager.storage.contracts;

import com.github.jlinqer.collections.List;
import com.honmodmanager.models.contracts.Mod;

/**
 *
 * @author Burgy Benjamin
 */
public interface Storage
{
    /**
     * Gets mods added to the storage.
     *
     * @return
     */
    List<Mod> getMods();

    /**
     * Adds a mod into the storage.
     *
     * @param mod
     */
    void addMod(Mod mod);

    /**
     * Clears all data saved in the storage.
     */
    void clear();
}
