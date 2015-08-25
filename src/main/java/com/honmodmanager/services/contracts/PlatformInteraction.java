package com.honmodmanager.services.contracts;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

/**
 *
 * @author Burgy Benjamin
 */
public interface PlatformInteraction
{
    void openLink(URI address) throws IOException;

    void openFolder(Path path) throws IOException;
}
