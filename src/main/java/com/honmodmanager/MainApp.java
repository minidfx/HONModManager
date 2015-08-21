package com.honmodmanager;

import com.honmodmanager.services.BootstrapperImpl;
import com.honmodmanager.services.contracts.Bootstrapper;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Burgy Benjamin
 */
final public class MainApp extends Application
{
    @Override
    public void start(Stage stage) throws IOException
    {
        Bootstrapper bootstrapper = new BootstrapperImpl();
        bootstrapper.run(stage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}
