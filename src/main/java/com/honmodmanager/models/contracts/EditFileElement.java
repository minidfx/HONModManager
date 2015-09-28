package com.honmodmanager.models.contracts;

import com.github.jlinqer.collections.List;
import com.honmodmanager.models.ConditionElement;

/**
 *
 * @author Burgy Benjamin
 */
public interface EditFileElement extends ConditionElement
{
    String getPath();

    List<EditOperation> getOperations();
}
