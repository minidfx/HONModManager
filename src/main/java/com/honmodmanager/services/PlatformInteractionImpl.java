package com.honmodmanager.services;

import com.honmodmanager.services.contracts.PlatformInteraction;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public final class PlatformInteractionImpl implements PlatformInteraction
{
    private final Desktop desktop;

    public PlatformInteractionImpl()
    {
        this.desktop = Desktop.getDesktop();
    }

    @Override
    public void openLink(URL address) throws IOException, URISyntaxException
    {
        this.desktop.browse(address.toURI());
    }
}
