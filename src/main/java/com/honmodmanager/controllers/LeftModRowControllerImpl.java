package com.honmodmanager.controllers;

import com.honmodmanager.controllers.contracts.LeftModRowController;
import com.honmodmanager.models.contracts.Mod;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public final class LeftModRowControllerImpl extends FXmlControllerBase implements LeftModRowController
{
    private static final Logger LOG = Logger.getLogger(LeftModRowControllerImpl.class.getName());
    private final Mod model;

    @FXML
    public Label name;

    @FXML
    public Label version;

    @FXML
    public ImageView icon;

    public LeftModRowControllerImpl(Mod model)
    {
        this.model = model;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.name.setText(this.model.getName());
        this.version.setText(this.model.getVersion().toString());
        this.icon.setImage(this.model.getIcon());
    }

    @Override
    public Mod getModel()
    {
        return this.model;
    }
}
