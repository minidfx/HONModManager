package com.honmodmanager.models;

import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.models.contracts.Version;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import javafx.scene.image.Image;

public final class ModImpl implements Mod
{
    private final String name;
    private final Version version;
    private final boolean isApplied;
    private Image icon;
    private Date date;
    public String description;
    private String author;
    private URI webLink;
    private URL webUpdateURL;
    private URL downloadURL;
    private Version modVersion;
    private Version gameVersion;
    private String modId;

    public ModImpl(String name, Version version, boolean isApplied)
    {
        this.name = name;
        this.version = version;
        this.isApplied = isApplied;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public Version getVersion()
    {
        return this.version;
    }

    @Override
    public String getId()
    {
        return this.modId;
    }

    @Override
    public boolean isApplied()
    {
        return this.isApplied;
    }

    @Override
    public Image getIcon()
    {
        return this.icon;
    }

    @Override
    public void setIcon(Image value)
    {
        this.icon = value;
    }

    @Override
    public Date getDate()
    {
        return this.date;
    }

    @Override
    public void setDate(Date value)
    {
        this.date = value;
    }

    @Override
    public void setAuthor(String value)
    {
        this.author = value;
    }

    @Override
    public String getAuthor()
    {
        return this.author;
    }

    @Override
    public URI getWebLink()
    {
        return this.webLink;
    }

    @Override
    public void setWebLink(URI value)
    {
        this.webLink = value;
    }

    @Override
    public void setUpdateURL(URL url)
    {
        this.webUpdateURL = url;
    }

    @Override
    public URL getUpdateURL()
    {
        return this.webUpdateURL;
    }

    @Override
    public URL getDownloadURL()
    {
        return this.downloadURL;
    }

    @Override
    public void setDownloadURL(URL value)
    {
        this.downloadURL = value;
    }

    @Override
    public Version getModManagerVersion()
    {
        return this.modVersion;
    }

    @Override
    public void setModManagerVersion(Version value)
    {
        this.modVersion = value;
    }

    @Override
    public Version getGameVersion()
    {
        return this.gameVersion;
    }

    @Override
    public void setGameVersion(Version value)
    {
        this.gameVersion = value;
    }

    @Override
    public void setId(String value)
    {
        this.modId = value;
    }
}
