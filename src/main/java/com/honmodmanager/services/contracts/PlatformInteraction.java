package com.honmodmanager.services.contracts;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

/**
 *
 * @author Burgy Benjamin
 */
public interface PlatformInteraction
{
    void openLink(URL address) throws IOException, URISyntaxException;

    void openFolder(Path path) throws IOException;
}
