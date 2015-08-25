package com.honmodmanager.exceptions;

import com.honmodmanager.models.contracts.Mod;

/**
 *
 * @author Burgy Benjamin
 */
public final class ModUpdateException extends Exception
{
    private final Mod mod;
    private final Exception innerException;

    public ModUpdateException(Mod mod, Exception innerException)
    {
        super();

        this.mod = mod;
        this.innerException = innerException;
    }

    @Override
    public String getMessage()
    {
        return this.innerException.getMessage();
    }

    public Exception getInnerException()
    {
        return innerException;
    }

    public Mod getMod()
    {
        return mod;
    }
}
