package com.honmodmanager.models;

import com.honmodmanager.models.contracts.Requierement;
import com.honmodmanager.models.contracts.Version;

public final class RequierementImpl implements Requierement
{
    private Version minium;
    private Version maximum;

    public RequierementImpl(Version minium)
    {
        this.minium = minium;
    }

    public RequierementImpl(Version minium, Version maximum)
    {
        this.minium = minium;
        this.maximum = maximum;
    }

    @Override
    public Version getMininum()
    {
        return this.minium;
    }

    @Override
    public Version getMaximum()
    {
        return this.maximum;
    }

    @Override
    public String toString()
    {
        return String.format("%s-%s", this.minium, this.maximum);
    }
}
