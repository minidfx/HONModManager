package com.honmodmanager.services;

import com.honmodmanager.models.contracts.Requierement;
import com.honmodmanager.models.contracts.Version;
import com.honmodmanager.services.contracts.RequierementValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public final class RequierementValidationImpl implements RequierementValidation
{
    @Override
    public boolean isValid(Version version, Requierement requierement)
    {
        Version minimumVersion = requierement.getMininum();
        Version maximumVersion = requierement.getMaximum();

        return version.greaterThan(minimumVersion)
               && version.lowerThan(maximumVersion)
               || version.isSame(minimumVersion)
               || version.isSame(maximumVersion);
    }
}
