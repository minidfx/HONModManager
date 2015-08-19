package com.honmodmanager.controllers;

import com.honmodmanager.controllers.contracts.LeftSideController;
import com.honmodmanager.models.contracts.Mod;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * The controller for communicating with the view representing the mods
 * installed in the game.
 *
 * @author Burgy Benjamin
 */
@Scope("singleton")
@Service
public final class LeftSideControllerImpl implements LeftSideController
{
    @FXML
    public ProgressIndicator progressIndicator;

    @FXML
    public ListView<Mod> listView;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }
}
