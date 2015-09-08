package com.honmodmanager.models.contracts;

import com.github.jlinqer.collections.List;

/**
 *
 * @author Burgy Benjamin
 */
public interface EditFileElement
{
    String getPath();

    String getCondition();

    List<EditOperation> getOperations();
}
