package com.honmodmanager.services.contracts;

import com.honmodmanager.exceptions.ModActivationException;
import com.honmodmanager.models.contracts.Mod;

/**
 *
 * @author Burgy Benjamin
 */
public interface ModEnabler
{
    void enable(Mod mod) throws ModActivationException;

    void disable(Mod mod);
}
