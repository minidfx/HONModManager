package com.honmodmanager.services.contracts;

import com.github.jlinqer.collections.List;
import com.honmodmanager.exceptions.ModSortException;
import com.honmodmanager.models.contracts.Mod;

/**
 *
 * @author Burgy Benjamin
 */
public interface ModSorter
{
    List<Mod> sort(List<Mod> mods) throws ModSortException;
}
