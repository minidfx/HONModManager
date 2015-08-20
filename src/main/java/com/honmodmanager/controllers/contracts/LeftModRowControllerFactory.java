package com.honmodmanager.controllers.contracts;

import com.honmodmanager.models.contracts.Mod;

/**
 *
 * @author Burgy Benjamin
 */
public interface LeftModRowControllerFactory
{
    LeftModRowController Create(Mod mod);
}
