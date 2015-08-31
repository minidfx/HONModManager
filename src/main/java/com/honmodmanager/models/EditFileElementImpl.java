package com.honmodmanager.models;

import com.github.jlinqer.collections.List;
import com.honmodmanager.models.contracts.EditFileElement;
import com.honmodmanager.models.contracts.EditOperation;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class EditFileElementImpl implements EditFileElement
{
    private EditOperation operation;
    private List<Node> operand;
    private String path;
    private String condition;

    public EditFileElementImpl(String path,
                               EditOperation operation,
                               List<Node> operand,
                               String condition)
    {
        this.path = path;
        this.operation = operation;
        this.operand = operand;
        this.condition = condition;
    }

    @Override
    public EditOperation getOperation()
    {
        return this.operation;
    }

    @Override
    public List<Node> getOperationOperand()
    {
        return this.operand;
    }

    @Override
    public String getPath()
    {
        return this.path;
    }

    @Override
    public String getCondition()
    {
        return this.condition;
    }
}
