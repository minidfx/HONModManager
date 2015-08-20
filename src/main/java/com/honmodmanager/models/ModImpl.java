package com.honmodmanager.models;

import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.models.contracts.Version;
import java.net.URI;
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

    public static String buildId(String modName)
    {
        char[] tempModName = modName.toCharArray();
        String modFixName = new String();

        for (int i = 0;
             i < tempModName.length;
             modFixName += Character.isLetter(tempModName[i++]) || Character.isDigit(tempModName[i - 1]) ? tempModName[i - 1] : "");

        return modFixName.toLowerCase();
    }

    public ModImpl(String name, Version version, boolean isApplied)
    {
        this.name = name;
        this.version = version;
        this.isApplied = isApplied;
    }

    public String getDescription()
    {
        return description;
    }

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
        return ModImpl.buildId(this.name);
    }

    @Override
    public URI getUpdateUri()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public URI getDownloadUri()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
}
