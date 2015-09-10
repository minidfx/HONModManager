package com.honmodmanager.models;

import com.honmodmanager.models.contracts.Version;

public final class VersionImpl implements Version
{
    private final int major;
    private final int minor;
    private final int fix;
    private final int build;
    private final boolean isNull;

    public VersionImpl()
    {
        this.major = 0;
        this.minor = 0;
        this.fix = 0;
        this.build = 0;

        this.isNull = true;
    }

    public VersionImpl(int major, int minor, int fix, int build)
    {
        this.major = major;
        this.minor = minor;
        this.fix = fix;
        this.build = build;

        this.isNull = major == 0 && minor == 0 && fix == 0 && build == 0;
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

    @Override
    public String toString()
    {
        return String.format("%d.%d.%d.%d", this.major, this.minor, this.fix, this.build);
    }

    @Override
    public boolean greaterThan(Version version)
    {
        if (this.isNull || version.isNull())
            return false;

        int foreignVersion = Integer.valueOf(String.format("%d%d%d%d",
                                                           version.getMajor(),
                                                           version.getMinor(),
                                                           version.getFix(),
                                                           version.getBuild()));
        int currentVersionInteger = Integer.valueOf(String.format("%d%d%d%d",
                                                                  this.getMajor(),
                                                                  this.getMinor(),
                                                                  this.getFix(),
                                                                  this.getBuild()));

        return currentVersionInteger > foreignVersion;
    }

    @Override
    public boolean lowerThan(Version version)
    {
        if (this.isNull || version.isNull())
            return false;

        int foreignVersion = Integer.valueOf(String.format("%d%d%d%d",
                                                           version.getMajor(),
                                                           version.getMinor(),
                                                           version.getFix(),
                                                           version.getBuild()));
        int currentVersionInteger = Integer.valueOf(String.format("%d%d%d%d",
                                                                  this.getMajor(),
                                                                  this.getMinor(),
                                                                  this.getFix(),
                                                                  this.getBuild()));

        return currentVersionInteger < foreignVersion;
    }

    @Override
    public boolean isNull()
    {
        return this.isNull;
    }

    @Override
    public boolean isSame(Version version)
    {
        if (this.isNull || version.isNull())
            return true;

        return this.major == version.getMajor()
               && this.minor == version.getMinor()
               && this.fix == version.getFix()
               && this.build == version.getBuild();
    }
}
