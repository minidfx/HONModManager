package com.honmodmanager.services.contracts;

import com.honmodmanager.models.contracts.Version;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.zip.ZipException;

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
    Path getFolderPath();

    /**
     * Returns the path of the executable.
     *
     * @return
     */
    Path getExecutablePath();

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
     * @throws java.io.FileNotFoundException
     * @throws java.util.zip.ZipException
     */
    Version getAdditionalResourceVersion() throws FileNotFoundException,
                                                  ZipException,
                                                  IOException,
                                                  ParseException;

    /**
     * Returns the path of the original resources.
     *
     * @return
     */
    Path getOriginalResourcePath();

    /**
     * Returns the path of the additional resources.
     *
     * @return
     */
    Path getAdditonalResourcePath();

    /**
     * Returns the path of the preferences folder.
     *
     * @return
     */
    Path getPreferencesFolderPath();
}
