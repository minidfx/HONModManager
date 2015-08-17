package com.honmodmanager;

import com.honmodmanager.controllers.HomeController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Burgy Benjamin
 */
public class MainApp extends Application
{
    @Override
    public void start(Stage stage) throws Exception
    {
        // Create the controller
        HomeController mainController = new HomeController();

        // Load the view 
        Parent root = mainController.loadView("/views/MainView.fxml").getRoot();

        // Create the main Scene
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}
