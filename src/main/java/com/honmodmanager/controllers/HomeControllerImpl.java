package com.honmodmanager.controllers;

import com.github.jlinqer.collections.List;
import com.honmodmanager.controllers.contracts.HomeController;
import com.honmodmanager.controllers.contracts.LeftSideController;
import com.honmodmanager.controllers.contracts.ModDetailsController;
import com.honmodmanager.controllers.contracts.ModDetailsControllerFactory;
import com.honmodmanager.events.ModSelectedEvent;
import com.honmodmanager.events.ModUpdatedEvent;
import com.honmodmanager.events.UpdateRowDisplayAction;
import com.honmodmanager.exceptions.ModUpdateException;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.models.contracts.Version;
import com.honmodmanager.services.contracts.EventAggregator;
import com.honmodmanager.services.contracts.EventAggregatorHandler;
import com.honmodmanager.services.contracts.GameInformation;
import com.honmodmanager.services.contracts.ModUpdater;
import com.honmodmanager.services.contracts.PlatformInteraction;
import com.honmodmanager.storage.contracts.Storage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import rx.Observable;
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
    private final Storage storage;

    @FXML
    private SplitPane mainSplitPane;

    @FXML
    private BorderPane leftPane;

    @FXML
    private BorderPane centerPane;

    @FXML
    private Label hONVersion;

    @FXML
    private Button updateAllButton;

    @Autowired
    public HomeControllerImpl(LeftSideController leftSideController,
                              ModDetailsControllerFactory modDetailsControllerFactory,
                              GameInformation gameInformation,
                              EventAggregator eventAggregator,
                              PlatformInteraction platformInteraction,
                              ModUpdater modUpdater,
                              Storage storage)
    {
        this.leftSideController = leftSideController;
        this.modDetailsControllerFactory = modDetailsControllerFactory;
        this.gameInformation = gameInformation;
        this.eventAggregator = eventAggregator;
        this.platformInteraction = platformInteraction;
        this.modUpdater = modUpdater;
        this.storage = storage;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.loadGameVersion();
        this.loadViews();

        this.eventAggregator.Subscribe(this);
    }

    @Override
    public void release()
    {
        super.release();

        this.eventAggregator.unsubscribe(this);
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

    @Override
    public void handleEvent(ModSelectedEvent event)
    {
        ModDetailsController controller = this.modDetailsControllerFactory.Create(event.getModel());

        try
        {
            FXMLLoader modDetailsControllerFxmlLoader = controller.loadView("/views/ModDetailsView.fxml");
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

        mergedObservable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.immediate())
                .subscribe(r ->
                        {
                            this.eventAggregator.Publish(new ModUpdatedEvent(r.getKey(), UpdateRowDisplayAction.Updated));
                }, e ->
                           {
                               if (e instanceof ModUpdateException)
                               {
                                   ModUpdateException exception = (ModUpdateException) e;

                                   this.eventAggregator.Publish(new ModUpdatedEvent(exception.getMod(),
                                                                                    UpdateRowDisplayAction.UpdateFailed));
                               }

                               LOG.log(Level.SEVERE, e.getMessage(), e);
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
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @FXML
    private void handleApplyAndRunAction(ActionEvent event)
    {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
