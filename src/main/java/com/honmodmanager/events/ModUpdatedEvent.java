package com.honmodmanager.events;

import com.honmodmanager.models.contracts.Mod;

/**
 *
 * @author Burgy Benjamin
 */
public final class ModUpdatedEvent
{
    private final Mod mod;
    private final UpdateRowDisplayAction action;

    public ModUpdatedEvent(Mod mod, UpdateRowDisplayAction action)
    {
        this.mod = mod;
        this.action = action;
    }

    public UpdateRowDisplayAction getAction()
    {
        return action;
    }

    public Mod getMod()
    {
        return mod;
    }
}
