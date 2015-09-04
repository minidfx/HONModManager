package com.honmodmanager.models;

import com.honmodmanager.models.contracts.ModRequierement;
import com.honmodmanager.models.contracts.Requierement;

public final class ModRequierementImpl implements ModRequierement
{
    private Requierement requierement;
    private String modId;

    public ModRequierementImpl(String modId, Requierement requierement)
    {
        this.requierement = requierement;
        this.modId = modId;
    }

    @Override
    public String getModId()
    {
        return this.modId;
    }

    @Override
    public Requierement getRequierement()
    {
        return this.requierement;
    }
}
