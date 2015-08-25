package com.honmodmanager.controllers;

import com.honmodmanager.controllers.contracts.FXmlController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;

/**
 *
 * @author Burgy Benjamin
 */
public abstract class FXmlControllerBase implements FXmlController
{
    protected FXMLLoader fxmlLoader;
    private boolean isReleased;

    public FXmlControllerBase()
    {
        this.isReleased = false;
    }

    @Override
    public FXMLLoader loadView(String viewPath) throws IOException
    {
        return this.fxmlLoader = FXmlController.super.loadView(viewPath);
    }

    @Override
    public FXMLLoader getFXMLoader()
    {
        return this.fxmlLoader;
    }

    @Override
    public void release()
    {
        this.isReleased = true;
    }

    @Override
    public boolean isReleased()
    {
        return this.isReleased;
    }
}
