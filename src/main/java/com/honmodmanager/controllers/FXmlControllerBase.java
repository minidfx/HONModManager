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
}
