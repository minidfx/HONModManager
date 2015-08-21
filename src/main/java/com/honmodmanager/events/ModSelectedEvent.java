package com.honmodmanager.events;

import com.honmodmanager.models.contracts.Mod;

/**
 *
 * @author Burgy Benjamin
 */
public final class ModSelectedEvent
{
    private final Mod model;

    public ModSelectedEvent(Mod model)
    {
        this.model = model;
    }

    public Mod getModel()
    {
        return this.model;
    }
}
