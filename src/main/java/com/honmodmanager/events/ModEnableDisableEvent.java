package com.honmodmanager.events;

import com.honmodmanager.models.contracts.Mod;

/**
 *
 * @author Burgy Benjamin
 */
public final class ModEnableDisableEvent
{
    private final Mod mod;

    public ModEnableDisableEvent(Mod mod)
    {
        this.mod = mod;
    }

    public Mod getMod()
    {
        return mod;
    }
}
