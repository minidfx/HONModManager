package com.honmodmanager.events;

import com.honmodmanager.models.contracts.Mod;

/**
 *
 * @author Burgy Benjamin
 */
public final class ModUpdatingEvent
{
    private final Mod mod;

    public ModUpdatingEvent(Mod mod)
    {
        this.mod = mod;
    }

    public Mod getMod()
    {
        return mod;
    }
}
