package com.honmodmanager.controllers;

import com.honmodmanager.controllers.contracts.LeftModRowController;
import com.honmodmanager.events.ModUpdateFailedEvent;
import com.honmodmanager.events.ModEnableDisableEvent;
import com.honmodmanager.events.ModUpdatedEvent;
import com.honmodmanager.events.ModUpdatingEvent;
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
import javafx.scene.layout.GridPane;

public final class LeftModRowControllerImpl extends FXmlControllerBase implements LeftModRowController
{
    private static final Logger LOG = Logger.getLogger(LeftModRowControllerImpl.class.getName());
    private final Mod model;
    private final EventAggregator eventAggregator;
    private EventAggregatorHandler<ModEnableDisableEvent> modEnableDisableEvent;
    private EventAggregatorHandler<ModUpdatingEvent> modUpdatingEvent;
    private EventAggregatorHandler<ModUpdateFailedEvent> modUpdateFailedEvent;
    private EventAggregatorHandler<ModUpdatedEvent> modUpdatedEvent;

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

    @FXML
    private GridPane rowPane;

    public LeftModRowControllerImpl(Mod model, EventAggregator eventAggregator)
    {
        this.model = model;
        this.eventAggregator = eventAggregator;
    }

    @Override
    public void release()
    {
        super.release();

        this.eventAggregator.unsubscribe(this.modUpdatingEvent);
        this.eventAggregator.unsubscribe(this.modUpdatedEvent);
        this.eventAggregator.unsubscribe(this.modUpdateFailedEvent);
        this.eventAggregator.unsubscribe(this.modEnableDisableEvent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.name.setText(this.model.getName());
        this.version.setText(this.model.getVersion().toString());
        this.icon.setImage(this.model.getIcon());
        this.enabled.setVisible(this.model.isEnabled());
        this.updateIndicator.setVisible(false);

        this.subscribeToEventAggregator();
    }

    private void subscribeToEventAggregator()
    {
        LeftModRowControllerImpl mainInstance = this;

        this.modUpdatingEvent = new EventAggregatorHandler<ModUpdatingEvent>()
        {
            @Override
            public void handleEvent(ModUpdatingEvent event)
            {
                if (event.getMod().equals(mainInstance.model))
                {
                    mainInstance.executeOnUIThread(() ->
                    {
                        mainInstance.rowPane.setDisable(true);
                        mainInstance.updateIndicator.setVisible(true);
                    });
                }
            }
        };

        this.modUpdateFailedEvent = new EventAggregatorHandler<ModUpdateFailedEvent>()
        {
            @Override
            public void handleEvent(ModUpdateFailedEvent event)
            {
                if (event.getMod().equals(mainInstance.model))
                {
                    mainInstance.executeOnUIThread(() ->
                    {
                        mainInstance.rowPane.setDisable(false);
                        mainInstance.updateIndicator.setVisible(false);
                    });
                }
            }
        };

        this.modEnableDisableEvent = new EventAggregatorHandler<ModEnableDisableEvent>()
        {
            @Override
            public void handleEvent(ModEnableDisableEvent event)
            {
                if (event.getMod().equals(mainInstance.model))
                {
                    mainInstance.executeOnUIThread(() ->
                    {
                        mainInstance.enabled.setVisible(event.getMod().isEnabled());
                    });
                }
            }
        };

        this.modUpdatedEvent = new EventAggregatorHandler<ModUpdatedEvent>()
        {
            @Override
            public void handleEvent(ModUpdatedEvent event)
            {
                if (event.getMod().equals(mainInstance.model))
                {
                    mainInstance.executeOnUIThread(() ->
                    {
                        mainInstance.rowPane.setDisable(false);
                        mainInstance.updateIndicator.setVisible(false);
                    });
                }
            }
        };

        this.eventAggregator.subscribe(this.modUpdatingEvent);
        this.eventAggregator.subscribe(this.modUpdatedEvent);
        this.eventAggregator.subscribe(this.modUpdateFailedEvent);
        this.eventAggregator.subscribe(this.modEnableDisableEvent);
    }

    @Override

    public Mod getModel()
    {
        return this.model;
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
