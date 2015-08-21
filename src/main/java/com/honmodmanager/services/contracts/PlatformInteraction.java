package com.honmodmanager.services.contracts;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author Burgy Benjamin
 */
public interface PlatformInteraction
{
    void openLink(URL address) throws IOException, URISyntaxException;
}
