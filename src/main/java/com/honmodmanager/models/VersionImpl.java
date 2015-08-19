package com.honmodmanager.models;

import com.honmodmanager.models.contracts.Version;

public class VersionImpl implements Version
{
    private final int major;
    private final int minor;
    private final int fix;
    private final int build;

    public VersionImpl(int major, int minor, int fix, int build)
    {
        this.major = major;
        this.minor = minor;
        this.fix = fix;
        this.build = build;
    }

    @Override
    public int getMajor()
    {
        return this.major;
    }

    @Override
    public int getMinor()
    {
        return this.minor;
    }

    @Override
    public int getFix()
    {
        return this.fix;
    }

    @Override
    public int getBuild()
    {
        return this.build;
    }
}
