package com.honmodmanager.contracts;

import javafx.stage.Stage;

/**
 *
 * @author Burgy Benjamin
 */
public interface ApplicationState
{
    Stage getPrimaryStage();

    void setPrimaryStage(Stage primaryStage);
}
