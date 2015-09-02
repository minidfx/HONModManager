package com.honmodmanager.services.contracts;

import rx.Observable;

/**
 * Enables the class to write mods installed on Heroes of Newerth.
 *
 * @author Burgy Benjamin
 */
public interface ModWriter
{
    Observable<Boolean> Write();
}
