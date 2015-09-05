package com.honmodmanager.events;

import com.honmodmanager.models.contracts.Mod;

/**
 *
 * @author Burgy Benjamin
 */
public final class ModActivationEvent
{
    private final Mod mod;

    public ModActivationEvent(Mod mod)
    {
        this.mod = mod;
    }

    public Mod getModel()
    {
        return mod;
    }
}
