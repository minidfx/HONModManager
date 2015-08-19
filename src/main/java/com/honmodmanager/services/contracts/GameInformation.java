package com.honmodmanager.services.contracts;

import com.honmodmanager.models.contracts.Version;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;

/**
 *
 * @author Burgy Benjamin
 */
public interface GameInformation
{
    /**
     * Returns the path of the game folder.
     *
     * @return
     */
    Path getFolder();

    /**
     * Returns the path of the executable.
     *
     * @return
     */
    Path getExecutable();

    /**
     * Returns the version of the game.
     *
     * @return
     * @throws java.io.FileNotFoundException
     * @throws java.text.ParseException
     */
    Version getVersion() throws FileNotFoundException, ParseException, IOException;

    /**
     * Returns the version of the original resource.
     *
     * @return
     */
    Version getOriginalResourceVersion();

    /**
     * Returns the path of the preferences folder.
     *
     * @return
     */
    Path getPreferencesFolder();

    /**
     * Returns the comment of the zip containing new resources.
     *
     * @return
     */
    String getZipComments();
}
