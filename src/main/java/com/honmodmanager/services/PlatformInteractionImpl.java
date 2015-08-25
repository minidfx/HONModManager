package com.honmodmanager.services;

import com.honmodmanager.services.contracts.PlatformInteraction;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
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
    public void openLink(URI address) throws IOException
    {
        this.desktop.browse(address);
    }

    @Override
    public void openFolder(Path path) throws IOException
    {
        this.desktop.open(path.toFile());
    }
}
