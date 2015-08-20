package com.honmodmanager.models.contracts;

import java.net.URI;
import java.util.Date;
import javafx.scene.image.Image;

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

    /**
     * Determines whether the mod is applied on the game.
     *
     * @return
     */
    boolean isApplied();

    /**
     * Returns the icon associated to the mod.
     *
     * @return
     */
    Image getIcon();

    /**
     * Sets the icon representing the mod.
     *
     * @param image
     */
    void setIcon(Image image);

    /**
     * Returns the date of the mod.
     *
     * @return
     */
    Date getDate();

    /**
     * Sets the date of the mod.
     *
     * @param value
     */
    void setDate(Date value);

    void setDescription(String description);

    String getDescription();
}
