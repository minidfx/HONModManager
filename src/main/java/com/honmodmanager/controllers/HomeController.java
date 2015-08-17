package com.honmodmanager.controllers;

import com.honmodmanager.controllers.contracts.FXmlController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * The controller for communicating with the view representing the Home.
 *
 * @author Burgy Benjamin
 */
public class HomeController implements FXmlController
{
    @FXML
    private Label label;

    @FXML
    private void handleButtonAction(ActionEvent event)
    {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }
}
