package com.honmodmanager.services.contracts;

import com.github.jlinqer.collections.List;
import com.honmodmanager.models.contracts.Mod;
import rx.Observable;

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
    Observable<Mod> getMods();

    /**
     * Returns the last result of the observable getMods.
     *
     * @return
     */
    List<Mod> getCachedMods();
}
