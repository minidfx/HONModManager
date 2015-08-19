package com.honmodmanager.services;

import com.honmodmanager.controllers.contracts.HomeController;
import com.honmodmanager.models.SizeImpl;
import com.honmodmanager.models.contracts.Size;
import com.honmodmanager.services.contracts.JavaFxStarter;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class JavaFxStarterImpl implements JavaFxStarter
{
    private static final Logger LOG = Logger.getLogger(JavaFxStarterImpl.class.getName());
    private final ClassPathXmlApplicationContext springApplicationContext;
    private final Preferences userPreferences;

    public JavaFxStarterImpl(ClassPathXmlApplicationContext classPathXmlApplicationContext)
    {
        this.springApplicationContext = classPathXmlApplicationContext;
        this.userPreferences = Preferences.userRoot().node("HoNModManager");
    }

    private Size getScreenSize()
    {
        double width = this.userPreferences.getDouble("WindowWidth", 800d);
        double height = this.userPreferences.getDouble("WindowHeight", 600d);

        return new SizeImpl(width, height);
    }

    private void ListenWindowSize(Stage stage)
    {
        stage.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
        {
            double width = newValue.doubleValue();

            this.userPreferences.putDouble("WindowWidth", width);
            LOG.info(String.format("Saved window width: %f", width));
        });

        stage.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
        {
            double height = newValue.doubleValue();

            this.userPreferences.putDouble("WindowHeight", height);
            LOG.info(String.format("Saved window height: %f", height));
        });
    }

    @Override
    public void show(Stage stage) throws IOException
    {
        LOG.info("Resolving and initializing the HomeController ...");

        // Create the main controller
        HomeController mainController = this.springApplicationContext.getBean(HomeController.class);

        // Load the view 
        Parent root = mainController.loadView("/views/MainView.fxml").getRoot();

        LOG.info("HomeController completely loaded.");

        // Create the main Scene
        Scene scene = new Scene(root);

        Size screenSize = this.getScreenSize();

        this.ListenWindowSize(stage);

        stage.setWidth(screenSize.getWidth());
        stage.setHeight(screenSize.getHeight());

        stage.setScene(scene);
        stage.show();

        LOG.info("Stage displayed.");
    }
}
