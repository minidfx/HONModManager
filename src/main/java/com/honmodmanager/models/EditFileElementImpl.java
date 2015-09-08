package com.honmodmanager.models;

import com.github.jlinqer.collections.List;
import com.honmodmanager.models.contracts.EditFileElement;
import com.honmodmanager.models.contracts.EditOperation;

public final class EditFileElementImpl implements EditFileElement
{
    private final List<EditOperation> operations;
    private final String path;
    private final String condition;

    public EditFileElementImpl(String path,
                               List<EditOperation> operations,
                               String condition)
    {
        this.operations = operations;
        this.condition = condition;
        
        this.path = path.replaceFirst("^(/?)", "");
    }

    @Override
    public List<EditOperation> getOperations()
    {
        return this.operations;
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
