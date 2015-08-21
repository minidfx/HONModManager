package com.honmodmanager.controllers;

import com.honmodmanager.controllers.contracts.ModDetailsController;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.services.contracts.PlatformInteraction;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
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
    private final PlatformInteraction platformInteraction;

    @FXML
    public Label title;

    @FXML
    public Label version;

    @FXML
    public Label description;

    @FXML
    public Hyperlink webLink;

    @FXML
    public Hyperlink downloadURL;

    @FXML
    public Hyperlink updateURL;

    @FXML
    public Label date;

    public ModDetailsControllerImpl(Mod model, PlatformInteraction platformInteraction)
    {
        this.model = model;
        this.platformInteraction = platformInteraction;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.title.setText(this.model.getName());
        this.version.setText(this.model.getVersion().toString());
        this.description.setText(this.model.getDescription());
        this.webLink.setText(this.model.getWebLink().toString());
        this.downloadURL.setText(this.model.getDownloadURL().toString());
        this.updateURL.setText(this.model.getUpdateURL().toString());

        Date modDate = this.model.getDate();

        if (modDate != null)
            this.date.setText(modDate.toString());
        else
            this.date.setText("");
    }

    @FXML
    private void handleWebLinkAction(ActionEvent event) throws IOException, URISyntaxException
    {
        this.platformInteraction.openLink(this.model.getWebLink().toURL());
    }

    @FXML
    private void handleDownloadAction(ActionEvent event) throws IOException, URISyntaxException
    {
        this.platformInteraction.openLink(this.model.getDownloadURL());
    }

    @FXML
    private void handleUpdateAction(ActionEvent event) throws IOException, URISyntaxException
    {
        this.platformInteraction.openLink(this.model.getUpdateURL());
    }

    @FXML
    private void handleUpdateButtonAction(ActionEvent event)
    {
        throw new UnsupportedOperationException();
    }
}
