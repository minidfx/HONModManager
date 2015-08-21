package com.honmodmanager.events;

import com.honmodmanager.models.contracts.Mod;

/**
 *
 * @author Burgy Benjamin
 */
public final class ModEnabledEvent
{
    private final Mod mod;

    public ModEnabledEvent(Mod mod)
    {
        this.mod = mod;
    }

    public Mod getMod()
    {
        return mod;
    }
}
