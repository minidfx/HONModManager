package com.honmodmanager.events;

import com.honmodmanager.models.contracts.Mod;

/**
 *
 * @author Burgy Benjamin
 */
public final class ModUpdateFailedEvent
{
    private final Mod mod;

    public ModUpdateFailedEvent(Mod mod)
    {
        this.mod = mod;
    }

    public Mod getMod()
    {
        return mod;
    }
}
