package com.honmodmanager.services.contracts;

import com.honmodmanager.models.contracts.Requirement;
import com.honmodmanager.models.contracts.Version;

/**
 *
 * @author Burgy Benjamin
 */
public interface RequirementValidation
{
    boolean isValid(Version version, Requirement requierement);
}
