package com.honmodmanager.controllers;

import com.honmodmanager.controllers.contracts.LeftModRowController;
import com.honmodmanager.events.ModUpdatedEvent;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.services.contracts.EventAggregator;
import com.honmodmanager.services.contracts.EventAggregatorHandler;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;

public final class LeftModRowControllerImpl extends FXmlControllerBase implements LeftModRowController,
                                                                                  EventAggregatorHandler<ModUpdatedEvent>
{
    private static final Logger LOG = Logger.getLogger(LeftModRowControllerImpl.class.getName());
    private final Mod model;
    private final EventAggregator eventAggregator;

    @FXML
    private Label name;

    @FXML
    private Label version;

    @FXML
    private ImageView icon;

    @FXML
    private ImageView enabled;

    @FXML
    private ProgressIndicator updateIndicator;

    public LeftModRowControllerImpl(Mod model, EventAggregator eventAggregator)
    {
        this.model = model;
        this.eventAggregator = eventAggregator;
    }

    @Override
    public void release()
    {
        super.release();

        this.eventAggregator.unsubscribe(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.name.setText(this.model.getName());
        this.version.setText(this.model.getVersion().toString());
        this.icon.setImage(this.model.getIcon());
        this.enabled.setVisible(this.model.isEnabled());
        this.updateIndicator.setVisible(false);

        this.eventAggregator.Subscribe(this);
    }

    @Override
    public Mod getModel()
    {
        return this.model;
    }

    @Override
    public void handleEvent(ModUpdatedEvent event)
    {
        Mod eventModel = event.getMod();
        if (eventModel == this.model)
        {
            Runnable updateUI = null;

            switch (event.getAction())
            {
                case EnableDisable:
                    updateUI = () ->
                            this.enabled.setVisible(eventModel.isEnabled());
                    break;

                case Updating:
                    updateUI = () -> this.updateIndicator.setVisible(true);
                    break;

                case UpdateFailed:
                case Updated:
                    updateUI = () -> this.updateIndicator.setVisible(false);
                    break;
            }

            if (updateUI != null)
            {
                this.executeOnUIThread(updateUI);
            }
        }
    }

    @Override
    public void refresh()
    {
        this.name.setText(this.model.getName());
        this.version.setText(this.model.getVersion().toString());
        this.icon.setImage(this.model.getIcon());
        this.enabled.setVisible(this.model.isEnabled());
    }
}
