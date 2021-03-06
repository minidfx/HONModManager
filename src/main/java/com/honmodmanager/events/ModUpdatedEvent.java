package com.honmodmanager.events;

import com.honmodmanager.models.contracts.Mod;

/**
 *
 * @author Burgy Benjamin
 */
public final class ModUpdatedEvent
{
    private final Mod mod;

    public ModUpdatedEvent(Mod mod)
    {
        this.mod = mod;
    }

    public Mod getMod()
    {
        return mod;
    }
}
