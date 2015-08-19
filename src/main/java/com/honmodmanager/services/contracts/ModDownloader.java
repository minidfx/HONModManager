package com.honmodmanager.services.contracts;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import rx.Observable;

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
     * @return
     * @throws java.net.MalformedURLException
     */
    Observable<File> Download(URI uri, String destination) throws MalformedURLException, IOException;
}
