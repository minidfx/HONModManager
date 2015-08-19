package com.honmodmanager.models;

import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.models.contracts.Version;
import java.net.URI;

public final class ModImpl implements Mod
{
    private final String name;
    private final Version version;

    public ModImpl(String name, Version version)
    {
        this.name = name;
        this.version = version;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
