package com.honmodmanager.controllers;

import com.honmodmanager.controllers.contracts.ModDetailsController;
import com.honmodmanager.models.contracts.Mod;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * The controller for communicating with the view containing the details of the
 * mod selected.
 *
 * @author Burgy Benjamin
 */
public final class ModDetailsControllerImpl extends FXmlControllerBase implements ModDetailsController
{
    private final Mod model;

    @FXML
    public Label title;

    @FXML
    public Label version;

    @FXML
    public Label description;

    public ModDetailsControllerImpl(Mod model)
    {
        this.model = model;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.title.setText(this.model.getName());
        this.version.setText(this.model.getVersion().toString());
        this.description.setText(this.model.getDescription());
    }
}
