package com.honmodmanager.controllers.contracts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

/**
 * Enables the class to be used as a controller.
 *
 * @author Burgy Benjamin
 */
public interface FXmlController extends Initializable
{
    /**
     * Returns the controller associated to the view.
     *
     * @param viewPath The absolute path of the view.
     * @return The view loaded.
     * @throws IOException
     */
    default FXMLLoader loadView(String viewPath) throws IOException
    {
        try
        {
            URL absoluteViewPath = this.getClass().getResource(viewPath);
            if (absoluteViewPath == null)
            {
                throw new FileNotFoundException(String.format("Cannot found the resource %s.", viewPath));
            }

            FXMLLoader fxmlLoader = new FXMLLoader(absoluteViewPath);

            fxmlLoader.setController(this);
            fxmlLoader.load();

            return fxmlLoader;
        }
        catch (NullPointerException e)
        {
            if (!Files.exists(Paths.get(viewPath), LinkOption.NOFOLLOW_LINKS))
            {
                throw new FileNotFoundException(viewPath);
            }

            throw e;
        }
    }
}
