package com.honmodmanager.models;

import com.github.jlinqer.collections.List;
import com.honmodmanager.models.contracts.EditOperation;
import com.honmodmanager.models.contracts.EditOperationType;
import javafx.util.Pair;

public final class EditOperationImpl implements EditOperation
{
    private final List<Pair<String, String>> attributes;
    private final EditOperationType operation;
    private final String text;

    public EditOperationImpl(EditOperationType operation, List<Pair<String, String>> attributes, String text)
    {
        this.operation = operation;
        this.attributes = attributes;
        this.text = text;
    }

    @Override
    public List<Pair<String, String>> getAttributes()
    {
        return this.attributes;
    }
    
    @Override
    public EditOperationType getOperationType()
    {
        return this.operation;
    }

    @Override
    public String getText()
    {
        return this.text;
    }   
}
