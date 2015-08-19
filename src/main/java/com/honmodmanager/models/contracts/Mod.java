package com.honmodmanager.models.contracts;

import java.net.URI;

/**
 * Represent a game mod.
 *
 * @author Burgy Benjamin
 */
public interface Mod
{
    /**
     * Gets the mod name.
     *
     * @return The mod name.
     */
    String getName();

    /**
     * Gets the mod version.
     *
     * @return The mod version.
     */
    Version getVersion();

    /**
     * Gets the mod Id.
     *
     * @return The Id representing the mod.
     */
    String getId();

    /**
     * Gets the URI of the update mod.
     *
     * @return
     */
    URI getUpdateUri();

    /**
     * Gets the URI of the download mod.
     *
     * @return
     */
    URI getDownloadUri();
}
