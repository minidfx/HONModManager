package com.honmodmanager.models.contracts;

/**
 *
 * @author Burgy Benjamin
 */
public interface Requirement
{
    Version getMininum();

    Version getMaximum();
}
