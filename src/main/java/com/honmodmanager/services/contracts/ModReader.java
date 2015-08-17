package com.honmodmanager.services.contracts;

import com.honmodmanager.models.contracts.Mod;
import java.util.List;

/**
 * Enables the class to read mods installed on Heroes of Newerth.
 *
 * @author Burgy Benjamin
 */
public interface ModReader
{
    /**
     * Retrieves mods that need an update.
     *
     * @return Returns mods that need an update.
     */
    List<Mod> getInstalledMods();
}
