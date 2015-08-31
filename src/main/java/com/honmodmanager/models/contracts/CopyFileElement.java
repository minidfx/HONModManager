package com.honmodmanager.models.contracts;

/**
 *
 * @author Burgy Benjamin
 */
public interface CopyFileElement
{
    String getPath();

    boolean overwrite();

    String getCondition();
}
