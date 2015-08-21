package com.honmodmanager.models.contracts;

import com.github.jlinqer.collections.Dictionary;
import com.github.jlinqer.collections.List;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import javafx.scene.image.Image;

/**
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
     * Gets the mod Id.
     *
     * @return The Id representing the mod.
     */
    String getId();

    /**
     * Sets the Id of the mod.
     *
     * @param value
     */
    void setId(String value);

    /**
     * Determines whether the mod is applied on the game.
     *
     * @return
     */
    boolean isEnabled();

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

    /**
     * Sets the description of the mod.
     *
     * @param description
     */
    void setDescription(String description);

    /**
     * Gets the description of the mods.
     *
     * @return
     */
    String getDescription();

    /**
     * Sets the name of the author.
     *
     * @param author
     */
    void setAuthor(String value);

    /**
     * Gets the name of the author.
     *
     * @return
     */
    String getAuthor();

    /**
     * Gets the URI containing information of the mod.
     *
     * @return
     */
    URI getWebLink();

    /**
     * Sets the URI containing information of the mod.
     *
     * @param value
     */
    void setWebLink(URI value);

    /**
     * Sets the URL to update the mod.
     *
     * @param value
     */
    void setUpdateURL(URL value);

    /**
     * Gets the URL to update the mod.
     *
     * @return
     */
    URL getUpdateURL();

    /**
     * Gets the URL to download the mod the first time.
     *
     * @return
     */
    URL getDownloadURL();

    /**
     * Sets the URL to download the mod the first time.
     *
     * @param value
     */
    void setDownloadURL(URL value);

    /**
     * Gets the version of the mod manager.
     *
     * @return
     */
    Version getModManagerVersion();

    /**
     * Sets the version of the mod manager.
     *
     * @param value
     */
    void setModManagerVersion(Version value);

    /**
     * Gets the version of the game.
     *
     * @return
     */
    Version getGameVersion();

    /**
     * Sets the version of the game.
     *
     * @param value
     */
    void setGameVersion(Version value);

    /**
     * Gets the version of the mod.
     *
     * @return
     */
    Version getVersion();

    List<String> getIncompatibillities();

    void addIncompatibillity(String modId);

    List<String> getRequiredMods();

    void addRequirement(String modId);

    Dictionary<String, Version> getApplyAfter();

    void addApplyAfter(String modId, Version version);

    Dictionary<String, Version> getApplyBefore();

    void addApplyBefore(String modId, Version version);

    void enabled(boolean value);
}
