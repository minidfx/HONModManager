package com.honmodmanager.models.contracts;

import com.honmodmanager.models.ConditionElement;

/**
 *
 * @author Burgy Benjamin
 */
public interface CopyFileElement extends ConditionElement
{
    String getPath();

    boolean overwrite();
}
