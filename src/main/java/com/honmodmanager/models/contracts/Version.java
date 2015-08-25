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
    int getFix();

    /**
     * Gets the build version,
     *
     * @return
     */
    int getBuild();

    /**
     * Determines whether the version passed in greater than the version.
     *
     * @param version
     * @return
     */
    boolean greaterThan(Version version);

    /**
     * Determines whether the version passed in lower than the version.
     *
     * @param version
     * @return
     */
    boolean lowerThan(Version version);

    /**
     * Determines whether the version is null, it means that you can't compare
     * this version with another.
     *
     * @return
     */
    boolean isNull();
}
