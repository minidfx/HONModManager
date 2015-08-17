package com.honmodmanager.models.contracts;

/**
 * Represents a version.
 *
 * @author Burgy Benjamin
 */
public interface Version
{
    /**
     * Gets the major version.
     *
     * @return
     */
    int getMajor();

    /**
     * Gets the minor version.
     *
     * @return
     */
    int getMinor();

    /**
     * Gets the hotfix version.
     *
     * @return
     */
    int getHotFix();

    /**
     * Gets the build version,
     *
     * @return
     */
    int getBuild();
}
