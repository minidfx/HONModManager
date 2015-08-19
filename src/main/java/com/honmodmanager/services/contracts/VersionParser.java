package com.honmodmanager.services.contracts;

import com.honmodmanager.models.contracts.Version;
import java.text.ParseException;

/**
 *
 * @author Burgy Benjamin
 */
public interface VersionParser
{
    /**
     * Tries to parse the version and returns the model representing the
     * version.
     *
     * @param version
     * @return
     */
    Version Parse(String version) throws ParseException;
}
