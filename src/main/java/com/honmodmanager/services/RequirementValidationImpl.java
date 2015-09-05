package com.honmodmanager.services;

import com.honmodmanager.models.contracts.Requirement;
import com.honmodmanager.models.contracts.Version;
import com.honmodmanager.services.contracts.RequirementValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public final class RequirementValidationImpl implements RequirementValidation
{
    @Override
    public boolean isValid(Version version, Requirement requirement)
    {
        Version minimumVersion = requirement.getMininum();
        Version maximumVersion = requirement.getMaximum();

        return version.greaterThan(minimumVersion)
               && version.lowerThan(maximumVersion)
               || version.isSame(minimumVersion)
               || version.isSame(maximumVersion);
    }
}
