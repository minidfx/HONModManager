package com.honmodmanager.controllers;

import com.honmodmanager.controllers.contracts.LeftSideController;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.services.contracts.ModReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import rx.Observable;

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
    private static final Logger LOG = Logger.getLogger(LeftSideControllerImpl.class.getName());
    private final ModReader modReader;

    @FXML
    public ProgressIndicator progressIndicator;

    @FXML
    public ListView<Mod> listMod;

    @Autowired
    public LeftSideControllerImpl(ModReader modReader)
    {
        this.modReader = modReader;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Observable<Mod> observableMods = this.modReader.getMods();

        // Subscribe to the observable for updating the list
        observableMods.subscribe((Mod m) ->
        {
            this.executeOnUIThread(() ->
            {
                this.listMod.getItems().add(m);
            });
        },
                                 e ->
                                 {
                                     LOG.log(Level.SEVERE, e.getMessage(), e);
                                 },
                                 () ->
                                 {
                                     this.executeOnUIThread(() ->
                                             {
                                                 this.progressIndicator.setVisible(false);
                                     });
                                 });
    }
}
