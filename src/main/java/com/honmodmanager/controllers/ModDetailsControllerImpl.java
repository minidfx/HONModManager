package com.honmodmanager.controllers;

import com.honmodmanager.controllers.contracts.ModDetailsController;
import com.honmodmanager.events.ModUpdatedEvent;
import com.honmodmanager.events.UpdateRowDisplayAction;
import com.honmodmanager.exceptions.ModActivationException;
import com.honmodmanager.models.contracts.Mod;
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
import rx.schedulers.Schedulers;

/**
 * The controller for communicating with the view containing the details of the
 * mod selected.
 *
 * @author Burgy Benjamin
 */
public final class ModDetailsControllerImpl extends FXmlControllerBase implements ModDetailsController,
                                                                                  EventAggregatorHandler<ModUpdatedEvent>
{
    private final Mod model;
    private final PlatformInteraction platformInteraction;
    private final EventAggregator eventAggregator;
    private static final Logger LOG = Logger.getLogger(ModDetailsControllerImpl.class.getName());
    private final ModUpdater modUpdater;

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
    private final ModEnabler modEnabler;

    public ModDetailsControllerImpl(Mod model,
                                    PlatformInteraction platformInteraction,
                                    EventAggregator eventAggregator,
                                    ModUpdater modUpdater,
                                    ModEnabler modEnabler)
    {
        this.model = model;
        this.platformInteraction = platformInteraction;
        this.eventAggregator = eventAggregator;
        this.modUpdater = modUpdater;
        this.modEnabler = modEnabler;
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

        Date modDate = this.model.getDate();

        if (modDate != null)
            this.date.setText(modDate.toString());
        else
            this.date.setText("");

        this.eventAggregator.Subscribe(this);
    }

    @Override
    public void release()
    {
        super.release();

        this.eventAggregator.unsubscribe(this);
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
        this.eventAggregator.Publish(new ModUpdatedEvent(this.model, UpdateRowDisplayAction.Updating));

        this.modUpdater.Update(this.model)
                .subscribeOn(Schedulers.io())
                .subscribe(x ->
                        {
                            this.eventAggregator.Publish(new ModUpdatedEvent(x.getKey(), UpdateRowDisplayAction.Updated));
                },
                           e ->
                           {
                               LOG.log(Level.SEVERE, e.getMessage(), e);
                           },
                           () ->
                           {
                               this.updateButton.setDisable(false);
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

            this.eventAggregator.Publish(new ModUpdatedEvent(mod, UpdateRowDisplayAction.EnableDisable));
        }
        catch (ModActivationException ex)
        {
            LOG.log(Level.WARNING, ex.getMessage(), ex);

            this.errorMessage.setVisible(true);
            this.errorMessage.setText(ex.getMessage());
            this.enabled.setSelected(!isEnabled);
        }
    }

    @Override
    public void handleEvent(ModUpdatedEvent event)
    {
        if (event.getMod() == this.model)
        {
            Runnable updateUI = null;

            switch (event.getAction())
            {
                case Updating:
                    updateUI = () -> this.updateButton.setDisable(true);
                    break;

                case Updated:
                    updateUI = () -> this.updateButton.setDisable(false);
                    break;
            }

            if (updateUI != null)
            {
                this.executeOnUIThread(updateUI);
            }
        }
    }
}
