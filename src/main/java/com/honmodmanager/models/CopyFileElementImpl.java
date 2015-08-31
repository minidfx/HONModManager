package com.honmodmanager.models;

import com.honmodmanager.models.contracts.CopyFileElement;

public final class CopyFileElementImpl implements CopyFileElement
{
    private String condition;
    private boolean overwrite;
    private String path;

    public CopyFileElementImpl(String path, boolean overwrite, String condition)
    {
        this.condition = condition;
        this.overwrite = overwrite;
        this.path = path;
    }

    @Override
    public String getPath()
    {
        return this.path;
    }

    @Override
    public boolean overwrite()
    {
        return this.overwrite;
    }

    @Override
    public String getCondition()
    {
        return this.condition;
    }
}
