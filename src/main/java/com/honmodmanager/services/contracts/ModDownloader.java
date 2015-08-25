package com.honmodmanager.services.contracts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 *
 * @author Burgy Benjamin
 */
public interface ModDownloader
{
    /**
     * Downloads and returns the object representing the file downloaded.
     *
     * @param uri
     * @param destination
     * @throws java.net.MalformedURLException
     * @throws java.io.FileNotFoundException
     */
    void download(URI uri, File destination) throws MalformedURLException,
                                                    FileNotFoundException,
                                                    IOException;

    byte[] download(URL url) throws MalformedURLException,
                                    FileNotFoundException,
                                    IOException;
}
