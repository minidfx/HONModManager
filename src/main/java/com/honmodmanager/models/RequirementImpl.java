package com.honmodmanager.models;

import com.honmodmanager.models.contracts.Requirement;
import com.honmodmanager.models.contracts.Version;

public final class RequirementImpl implements Requirement
{
    private final Version minium;
    private final Version maximum;

    public RequirementImpl(Version minium)
    {
        this.minium = minium;
        this.maximum = minium;
    }

    public RequirementImpl(Version minium, Version maximum)
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
