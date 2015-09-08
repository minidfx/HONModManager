package com.honmodmanager.controllers;

import com.honmodmanager.controllers.contracts.ModDetailsController;
import com.honmodmanager.events.ModCleanedEvent;
import com.honmodmanager.events.ModEnableDisableEvent;
import com.honmodmanager.events.ModUpdatedEvent;
import com.honmodmanager.events.ModUpdatingEvent;
import com.honmodmanager.exceptions.ModActivationException;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.services.contracts.ConnectionTester;
import com.honmodmanager.services.contracts.EventAggregator;
import com.honmodmanager.services.contracts.EventAggregatorHandler;
import com.honmodmanager.services.contracts.ModEnabler;
import com.honmodmanager.services.contracts.ModUpdater;
import com.honmodmanager.services.contracts.PlatformInteraction;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * The controller for communicating with the view containing the details of the
 * mod selected.
 *
 * @author Burgy Benjamin
 */
public final class ModDetailsControllerImpl extends FXmlControllerBase implements ModDetailsController
{
    private static final Logger LOG = Logger.getLogger(ModDetailsControllerImpl.class.getName());
    private EventAggregatorHandler<ModCleanedEvent> modCleanedEvent;
    private EventAggregatorHandler<ModUpdatingEvent> modUpdatingEvent;
    private EventAggregatorHandler<ModUpdatedEvent> modUpdatedEvent;
    
    private final Mod model;
    private final PlatformInteraction platformInteraction;
    private final EventAggregator eventAggregator;
    private final ModUpdater modUpdater;
    private final ModEnabler modEnabler;
    private final ConnectionTester connectionTester;

    private Subscription internetConnectionObservable;

    @FXML
    private Label title;

    @FXML
    private Label version;

    @FXML
    private Label description;

    @FXML
    private Hyperlink webLink;

    @FXML
    private Hyperlink downloadURL;

    @FXML
    private Hyperlink updateURL;

    @FXML
    private ToggleButton enabled;

    @FXML
    private Label date;

    @FXML
    private Label errorMessage;

    @FXML
    private Button updateButton;

    public ModDetailsControllerImpl(Mod model,
                                    PlatformInteraction platformInteraction,
                                    EventAggregator eventAggregator,
                                    ModUpdater modUpdater,
                                    ModEnabler modEnabler,
                                    ConnectionTester connectionTester)
    {
        this.model = model;
        this.platformInteraction = platformInteraction;
        this.eventAggregator = eventAggregator;
        this.modUpdater = modUpdater;
        this.modEnabler = modEnabler;
        this.connectionTester = connectionTester;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.title.setText(this.model.getName());
        this.version.setText(this.model.getVersion().toString());
        this.description.setText(this.model.getDescription());
        this.webLink.setText(this.model.getWebLink().toString());
        this.downloadURL.setText(this.model.getDownloadAddress().toString());
        this.updateURL.setText(this.model.getVersionAddress().toString());
        this.enabled.setSelected(this.model.isEnabled());
        this.errorMessage.setVisible(false);
        this.updateButton.setDisable(true);

        Date modDate = this.model.getDate();

        if (modDate != null)
            this.date.setText(modDate.toString());
        else
            this.date.setText("");

        this.internetConnectionObservable = this.connectionTester
                .TestUrl(this.model.getVersionAddress())
                .subscribeOn(Schedulers.io())
                .subscribe(b ->
                        {
                            this.executeOnUIThread(() ->
                                    {
                                        LOG.info(String.format("Update URL status: %b", b));
                                        this.updateButton.setDisable(!b);
                            });
                });

        this.subscribeToEventAggregator();
    }

    private void subscribeToEventAggregator()
    {
        ModDetailsControllerImpl mainInstance = this;

        this.modUpdatedEvent = new EventAggregatorHandler<ModUpdatedEvent>()
        {
            @Override
            public void handleEvent(ModUpdatedEvent event)
            {
                if(event.getMod().equals(mainInstance.model))
                {
                    mainInstance.executeOnUIThread(() ->
                    {
                        mainInstance.updateButton.setDisable(false);
                    });
                }
            }
        };
        
        this.modUpdatingEvent = new EventAggregatorHandler<ModUpdatingEvent>()
        {
            @Override
            public void handleEvent(ModUpdatingEvent event)
            {
                if(event.getMod().equals(mainInstance.model))
                {
                    mainInstance.updateButton.setDisable(true);
                }
            }
        };
        
        this.modCleanedEvent = new EventAggregatorHandler<ModCleanedEvent>()
        {
            @Override
            public void handleEvent(ModCleanedEvent event)
            {
                mainInstance.executeOnUIThread(() ->
                {
                    mainInstance.refresh();
                });
            }
        };

        this.eventAggregator.subscribe(this.modUpdatedEvent);
        this.eventAggregator.subscribe(this.modUpdatingEvent);
        this.eventAggregator.subscribe(this.modCleanedEvent);
    }

    @Override
    public void release()
    {
        super.release();

        this.eventAggregator.unsubscribe(this.modUpdatedEvent);
        this.eventAggregator.unsubscribe(this.modUpdatingEvent);
        this.eventAggregator.unsubscribe(this.modCleanedEvent);

        this.internetConnectionObservable.unsubscribe();
    }
    
    private void refresh()
    {
        this.title.setText(this.model.getName());
        this.version.setText(this.model.getVersion().toString());
        this.description.setText(this.model.getDescription());
        this.webLink.setText(this.model.getWebLink().toString());
        this.downloadURL.setText(this.model.getDownloadAddress().toString());
        this.updateURL.setText(this.model.getVersionAddress().toString());
        this.enabled.setSelected(this.model.isEnabled());
    }

    @FXML
    private void handleWebLinkAction(ActionEvent event)
    {
        try
        {
            this.platformInteraction.openLink(this.model.getWebLink());
        }
        catch (IOException ex)
        {
            LOG.log(Level.WARNING, ex.getMessage(), ex);
        }
    }

    @FXML
    private void handleDownloadAction(ActionEvent event)
    {
        try
        {
            this.platformInteraction.openLink(this.model.getDownloadAddress());
        }
        catch (IOException ex)
        {
            LOG.log(Level.WARNING, ex.getMessage(), ex);
        }
    }

    @FXML
    private void handleUpdateAction(ActionEvent event)
    {
        try
        {
            this.platformInteraction.openLink(this.model.getVersionAddress());
        }
        catch (IOException ex)
        {
            LOG.log(Level.WARNING, ex.getMessage(), ex);
        }
    }

    @FXML
    private void handleUpdateButtonAction(ActionEvent event) throws IOException,
                                                                    MalformedURLException,
                                                                    ParseException
    {
        this.updateButton.setDisable(true);
        this.eventAggregator.publish(new ModUpdatingEvent(this.model));

        this.modUpdater.Update(this.model)
                .subscribeOn(Schedulers.io())
                .subscribe(x ->
                        {
                            this.eventAggregator.publish(new ModUpdatedEvent(x.getKey()));
                },
                           e ->
                           {
                               LOG.log(Level.SEVERE, e.getMessage(), e);
                           },
                           () ->
                           {
                               this.executeOnUIThread(() ->
                                       this.updateButton.setDisable(false));
                           });
    }

    @FXML
    private void handleEnableAction(ActionEvent event)
    {
        boolean isEnabled = this.enabled.isSelected();
        Mod mod = this.model;

        try
        {
            if (isEnabled)
                this.modEnabler.enable(mod);
            else
                this.modEnabler.disable(mod);

            this.eventAggregator.publish(new ModEnableDisableEvent(mod));
        }
        catch (ModActivationException ex)
        {
            LOG.log(Level.WARNING, ex.getMessage(), ex);

            this.errorMessage.setVisible(true);
            this.errorMessage.setText(ex.getMessage());
            this.enabled.setSelected(!isEnabled);
        }
    }
}
