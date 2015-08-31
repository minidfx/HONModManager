package com.honmodmanager.models.contracts;

import com.github.jlinqer.collections.List;
import org.w3c.dom.Node;

/**
 *
 * @author Burgy Benjamin
 */
public interface EditFileElement
{
    EditOperation getOperation();

    String getPath();

    String getCondition();

    List<Node> getOperationOperand();
}
