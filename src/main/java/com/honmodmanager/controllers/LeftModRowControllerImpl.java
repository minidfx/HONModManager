package com.honmodmanager.controllers;

import com.honmodmanager.controllers.contracts.LeftModRowController;
import com.honmodmanager.events.ModEnabledEvent;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.services.contracts.EventAggregator;
import com.honmodmanager.services.contracts.EventAggregatorHandler;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public final class LeftModRowControllerImpl extends FXmlControllerBase implements LeftModRowController,
                                                                                  EventAggregatorHandler<ModEnabledEvent>
{
    private static final Logger LOG = Logger.getLogger(LeftModRowControllerImpl.class.getName());
    private final Mod model;
    private final EventAggregator eventAggregator;

    @FXML
    public Label name;

    @FXML
    public Label version;

    @FXML
    public ImageView icon;

    @FXML
    public ImageView enabled;

    public LeftModRowControllerImpl(Mod model, EventAggregator eventAggregator)
    {
        this.model = model;
        this.eventAggregator = eventAggregator;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.name.setText(this.model.getName());
        this.version.setText(this.model.getVersion().toString());
        this.icon.setImage(this.model.getIcon());
        this.enabled.setVisible(this.model.isEnabled());

        this.eventAggregator.Subscribe(this);
    }

    @Override
    public Mod getModel()
    {
        return this.model;
    }

    @Override
    public void handleEvent(ModEnabledEvent event)
    {
        Mod eventModel = event.getMod();
        if (eventModel.getId().equals(this.model.getId()))
        {
            this.enabled.setVisible(eventModel.isEnabled());
        }
    }
}
