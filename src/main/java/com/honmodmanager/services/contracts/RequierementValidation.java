package com.honmodmanager.services.contracts;

import com.honmodmanager.models.contracts.Requierement;
import com.honmodmanager.models.contracts.Version;

/**
 *
 * @author Burgy Benjamin
 */
public interface RequierementValidation
{
    boolean isValid(Version version, Requierement requierement);
}
