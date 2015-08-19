package com.honmodmanager;

import com.honmodmanager.controllers.contracts.HomeController;
import com.sun.istack.internal.logging.Logger;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Burgy Benjamin
 */
final public class MainApp extends Application
{
    private static ClassPathXmlApplicationContext springApplicationContext;
    private static final Logger logger = Logger.getLogger(MainApp.class);

    @Override
    public void start(Stage stage) throws Exception
    {
        logger.info("Resolving and initializing the HomeController ...");

        // Create the main controller
        HomeController mainController = springApplicationContext.getBean(HomeController.class);

        // Load the view 
        Parent root = mainController.loadView("/views/MainView.fxml").getRoot();

        logger.info("HomeController completely loaded.");

        // Create the main Scene
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

        logger.info("Application started.");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        logger.info("Starting application ...");

        // Create the Spring context
        springApplicationContext = new ClassPathXmlApplicationContext("/spring/applicationContext.xml");

        launch(args);
    }
}
