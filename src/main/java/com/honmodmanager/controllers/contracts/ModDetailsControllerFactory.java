package com.honmodmanager.controllers.contracts;

import com.honmodmanager.models.contracts.Mod;

/**
 *
 * @author Burgy Benjamin
 */
public interface ModDetailsControllerFactory
{
    ModDetailsController Create(Mod model);
}
