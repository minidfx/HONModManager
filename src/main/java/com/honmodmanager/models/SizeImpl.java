package com.honmodmanager.models;

import com.honmodmanager.models.contracts.Size;

public final class SizeImpl implements Size
{
    private final double width;
    private final double height;

    public SizeImpl(double width, double height)
    {
        this.width = width;
        this.height = height;
    }

    @Override
    public double getWidth()
    {
        return this.width;
    }

    @Override
    public double getHeight()
    {
        return this.height;
    }
}
