package com.honmodmanager.controllers;

import com.honmodmanager.controllers.contracts.HomeController;
import com.honmodmanager.controllers.contracts.LeftSideController;
import com.honmodmanager.controllers.contracts.ModDetailsController;
import com.honmodmanager.models.contracts.Version;
import com.honmodmanager.services.contracts.GameInformation;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import rx.Observable;

/**
 * The controller for communicating with the view representing the Home.
 *
 * @author Burgy Benjamin
 */
@Scope("singleton")
@Service
public final class HomeControllerImpl extends FXmlControllerBase implements HomeController
{
    private final LeftSideController leftSideController;
    private final ModDetailsController modDetailsController;
    private static final Logger LOG = Logger.getLogger(HomeControllerImpl.class.getName());

    @FXML
    public SplitPane mainSplitPane;

    @FXML
    public BorderPane leftPane;

    @FXML
    public BorderPane centerPane;

    @FXML
    public Label hONVersion;
    private final GameInformation gameInformation;

    @Autowired
    public HomeControllerImpl(LeftSideController leftSideController,
                              ModDetailsController modDetailsController,
                              GameInformation gameInformation)
    {
        this.leftSideController = leftSideController;
        this.modDetailsController = modDetailsController;
        this.gameInformation = gameInformation;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.loadGameVersion();
        this.loadViews();
    }

    private void loadViews()
    {
        try
        {
            FXMLLoader leftView = this.leftSideController.loadView("/views/LeftSideView.fxml");
            this.leftPane.setCenter(leftView.getRoot());
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            // TODO: Display an error message when a FATAL error occured here.
        }
    }

    private void loadGameVersion()
    {
        this.executeOnUIThread(() ->
        {
            this.hONVersion.setText("...");
        });

        Observable<Version> gameVersionObservable = this.gameInformation.getVersion();
        gameVersionObservable.subscribe(v ->
        {
            this.executeOnUIThread(() ->
            {
                this.hONVersion.setText(v.toString());
            });
        },
                                        e ->
                                        {
                                            LOG.log(Level.SEVERE, e.getMessage(), e);
                                            this.executeOnUIThread(() ->
                                                    {
                                                        this.hONVersion.setText("Error!");
                                            });
                                        });
    }
}
