package com.honmodmanager.controllers;

import com.github.jlinqer.collections.List;
import com.honmodmanager.controllers.contracts.HomeController;
import com.honmodmanager.controllers.contracts.LeftSideController;
import com.honmodmanager.controllers.contracts.ModDetailsController;
import com.honmodmanager.controllers.contracts.ModDetailsControllerFactory;
import com.honmodmanager.events.ModSelectedEvent;
import com.honmodmanager.events.ModUpdatedEvent;
import com.honmodmanager.events.UpdateRowDisplayAction;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.models.contracts.Version;
import com.honmodmanager.services.contracts.ConnectionTester;
import com.honmodmanager.services.contracts.EventAggregator;
import com.honmodmanager.services.contracts.EventAggregatorHandler;
import com.honmodmanager.services.contracts.GameInformation;
import com.honmodmanager.services.contracts.ModUpdater;
import com.honmodmanager.services.contracts.ModWriter;
import com.honmodmanager.services.contracts.PlatformInteraction;
import com.honmodmanager.storage.contracts.Storage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * The controller for communicating with the view representing the Home.
 *
 * @author Burgy Benjamin
 */
@Scope("singleton")
@Service
public final class HomeControllerImpl extends FXmlControllerBase implements HomeController,
                                                                            EventAggregatorHandler<ModSelectedEvent>
{
    private static final Logger LOG = Logger.getLogger(HomeControllerImpl.class.getName());

    private final LeftSideController leftSideController;
    private final GameInformation gameInformation;
    private final ModDetailsControllerFactory modDetailsControllerFactory;
    private final EventAggregator eventAggregator;
    private final PlatformInteraction platformInteraction;
    private final ModUpdater modUpdater;
    private final ModWriter modWriter;
    private final Storage storage;
    private final ConnectionTester connectionTester;

    private Subscription writerSubscription;
    private Subscription gameVersionSubscription;
    private Subscription updateSubscription;
    private ModDetailsController selectedModDetailsController;

    @FXML
    private Button applyButton;

    @FXML
    private BorderPane leftPane;

    @FXML
    private BorderPane centerPane;

    @FXML
    private Label hONVersion;

    @FXML
    private Button updateAllButton;
    private Subscription internetConnectionObservable;

    @Autowired
    public HomeControllerImpl(LeftSideController leftSideController,
                              ModDetailsControllerFactory modDetailsControllerFactory,
                              GameInformation gameInformation,
                              EventAggregator eventAggregator,
                              PlatformInteraction platformInteraction,
                              ModUpdater modUpdater,
                              ModWriter modWriter,
                              Storage storage,
                              ConnectionTester connectionTester)
    {
        this.leftSideController = leftSideController;
        this.modDetailsControllerFactory = modDetailsControllerFactory;
        this.gameInformation = gameInformation;
        this.eventAggregator = eventAggregator;
        this.platformInteraction = platformInteraction;
        this.modUpdater = modUpdater;
        this.modWriter = modWriter;
        this.storage = storage;
        this.connectionTester = connectionTester;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.loadGameVersion();
        this.loadViews();

        this.updateAllButton.setDisable(true);
        this.eventAggregator.Subscribe(this);

        try
        {
            this.internetConnectionObservable = this.connectionTester
                    .TestUrl(new URI("http://www.google.com"))
                    .subscribeOn(Schedulers.io())
                    .subscribe(b ->
                            {
                                this.executeOnUIThread(() ->
                                        {
                                            LOG.info(String.format("Googe host status: %b", b));
                                            this.updateAllButton.setDisable(!b);
                                });
                    });
        }
        catch (URISyntaxException ex)
        {
            LOG.log(Level.WARNING, ex.getMessage(), ex);
        }
    }

    @Override
    public void release()
    {
        super.release();

        this.eventAggregator.unsubscribe(this);
        this.gameVersionSubscription.unsubscribe();
        this.updateSubscription.unsubscribe();
        this.writerSubscription.unsubscribe();
        this.internetConnectionObservable.unsubscribe();
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
        this.gameVersionSubscription = gameVersionObservable.subscribe(v ->
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

    @Override
    public void handleEvent(ModSelectedEvent event)
    {
        if (this.selectedModDetailsController != null)
        {
            this.selectedModDetailsController.release();
        }

        this.selectedModDetailsController = this.modDetailsControllerFactory.Create(event.getModel());

        try
        {
            FXMLLoader modDetailsControllerFxmlLoader = selectedModDetailsController.loadView("/views/ModDetailsView.fxml");
            this.executeOnUIThread(() ->
            {
                this.centerPane.setCenter(modDetailsControllerFxmlLoader.getRoot());
            });
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @FXML
    private void handleOpenModsFolderAction(ActionEvent event)
    {
        try
        {
            Path modsFolder = this.gameInformation.getModsFolder();
            this.platformInteraction.openFolder(modsFolder);
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @FXML
    private void handleUpdateAllAction(ActionEvent event) throws IOException,
                                                                 MalformedURLException,
                                                                 ParseException
    {
        this.executeOnUIThread(() ->
        {
            this.updateAllButton.setDisable(true);
        });

        List<Mod> mods = this.storage.getMods();
        List<Observable<Pair<Mod, File>>> observables = new List<>();

        for (Mod mod : mods)
        {
            observables.add(this.modUpdater.Update(mod));
            this.eventAggregator.Publish(new ModUpdatedEvent(mod, UpdateRowDisplayAction.Updating));
        }

        Observable<Pair<Mod, File>> mergedObservable = Observable.merge(observables, Schedulers.io());

        this.updateSubscription = mergedObservable.subscribeOn(Schedulers.io())
                .subscribe(r ->
                        {
                            this.eventAggregator.Publish(new ModUpdatedEvent(r.getKey(), UpdateRowDisplayAction.Updated));
                }, e ->
                           {
                               LOG.log(Level.SEVERE, e.getMessage(), e);

                               for (Mod mod : mods)
                               {
                                   this.eventAggregator.Publish(new ModUpdatedEvent(mod,
                                                                                    UpdateRowDisplayAction.UpdateFailed));
                               }

                               this.executeOnUIThread(() ->
                                       {
                                           this.updateAllButton.setDisable(false);
                               });
                },
                           () ->
                           {
                               this.executeOnUIThread(() ->
                                       {
                                           this.updateAllButton.setDisable(false);
                               });
                           });
    }

    @FXML
    private void handleApplyAction(ActionEvent event)
    {
        this.applyButton.setDisable(true);

        this.writerSubscription = this.modWriter
                .Write()
                .subscribeOn(Schedulers.io())
                .subscribe(x ->
                        {
                            if (x)
                            {

                            }
                            else
                            {

                            }
                }, e ->
                           {
                               LOG.log(Level.SEVERE, e.getMessage(), e);
                },
                           () ->
                           {
                               this.executeOnUIThread(() ->
                                       this.applyButton.setDisable(false));
                           });
    }

    @FXML
    private void handleApplyAndRunAction(ActionEvent event)
    {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
