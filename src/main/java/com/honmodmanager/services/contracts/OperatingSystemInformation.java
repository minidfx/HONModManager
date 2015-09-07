package com.honmodmanager.services.contracts;

/**
 *
 * @author Burgy Benjamin
 */
public interface OperatingSystemInformation
{
    /**
     * Returns the operating system identified.
     *
     * @return The enumeration giving the information.
     */
    OperatingSystem getOperatingSystem();

    /**
     * Determines whether the operating system is x64 architecture.
     *
     * @return Returns true whether the operating is x64 architecture otherwise
     * false.
     */
    boolean isX64();
}
