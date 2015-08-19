package com.honmodmanager;

import com.honmodmanager.services.JavaFxStarterImpl;
import com.honmodmanager.services.contracts.JavaFxStarter;
import com.sun.istack.internal.logging.Logger;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Burgy Benjamin
 */
final public class MainApp extends Application
{
    private static ClassPathXmlApplicationContext springApplicationContext;
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(MainApp.class.getName());

    @Override
    public void start(Stage stage) throws IOException
    {
        JavaFxStarter starter = new JavaFxStarterImpl(MainApp.springApplicationContext);
        starter.show(stage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        LOG.info("Starting application ...");

        // Create the Spring context
        springApplicationContext = new ClassPathXmlApplicationContext("/spring/applicationContext.xml");

        launch(args);
    }
}
