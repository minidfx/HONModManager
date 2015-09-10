package com.honmodmanager.services.contracts;

import com.github.jlinqer.linq.IEnumerable;
import com.honmodmanager.exceptions.ModSortException;
import com.honmodmanager.models.contracts.Mod;

/**
 *
 * @author Burgy Benjamin
 */
public interface ModSorter
{
    IEnumerable<Mod> sort(IEnumerable<Mod> mods) throws ModSortException;
}
