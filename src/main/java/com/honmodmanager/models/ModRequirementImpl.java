package com.honmodmanager.models;

import com.honmodmanager.models.contracts.ModRequirement;
import com.honmodmanager.models.contracts.Requirement;

public final class ModRequirementImpl implements ModRequirement
{
    private final Requirement requirement;
    private final String modId;

    public ModRequirementImpl(String modId, Requirement requirement)
    {
        this.requirement = requirement;
        this.modId = modId;
    }

    @Override
    public String getModId()
    {
        return this.modId;
    }

    @Override
    public Requirement getRequirement()
    {
        return this.requirement;
    }
}
