package com.honmodmanager.services.contracts;

import com.honmodmanager.models.contracts.Mod;

/**
 * Enables the class to manage mods installed on Heroes of Newerth.
 *
 * @author Burgy Benjamin
 */
public interface ModManager
{
    /**
     * Insert the mod into the game.
     *
     * @param mod Represents a game mod.
     */
    void Add(Mod mod);

    /**
     * Removes a mod installed in the game.
     *
     * @param mod
     */
    void Remove(Mod mod);
}
