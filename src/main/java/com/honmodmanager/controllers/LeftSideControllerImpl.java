package com.honmodmanager.controllers;

import com.honmodmanager.events.ModSelectedEvent;
import com.github.jlinqer.collections.List;
import com.honmodmanager.controllers.contracts.LeftModRowController;
import com.honmodmanager.controllers.contracts.LeftModRowControllerFactory;
import com.honmodmanager.controllers.contracts.LeftSideController;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.services.contracts.EventAggregator;
import com.honmodmanager.services.contracts.ModManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
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
public final class LeftSideControllerImpl extends FXmlControllerBase implements LeftSideController
{
    private static final Logger LOG = Logger.getLogger(LeftSideControllerImpl.class.getName());

    private final List<LeftModRowController> controllers;
    private final ModManager modManager;
    private final LeftModRowControllerFactory leftModRowControllerFactory;
    private final EventAggregator eventAggregator;

    @FXML
    public BorderPane progressIndicator;

    @FXML
    public ListView<Parent> listMod;

    @Autowired
    public LeftSideControllerImpl(ModManager modManager,
                                  LeftModRowControllerFactory leftModRowControllerFactory,
                                  EventAggregator eventAggregator,
    {
        this.controllers = new List<>();

        this.modManager = modManager;
        this.leftModRowControllerFactory = leftModRowControllerFactory;
        this.eventAggregator = eventAggregator;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.fillMods();
        this.listenSelection();
    }

    @Override
    public void release()
    {
        super.release();

        this.eventAggregator.unsubscribe(this);
    }

    private void listenSelection()
    {
        this.listMod.getSelectionModel().selectedItemProperty().addListener((
                ObservableValue<? extends Parent> observable,
                Parent oldValue,
                Parent newValue) ->
                {
                    LeftModRowController controller = this.controllers.single(c ->
                            {
                                return c.getFXMLoader().getRoot().equals(newValue);
                    });

                    Mod modSelected = controller.getModel();
                    LOG.info(String.format("Mod %s selected.", modSelected.getName()));

                    // Notify the controller responsible for displaying details of the mod selected.
                    this.eventAggregator.Publish(new ModSelectedEvent(modSelected));
                });
    }

    private void fillMods()
    {
        Observable<Mod> observableMods = this.modManager.getAll();

        // Subscribe to the observable for updating the list
        observableMods.subscribe((Mod m) ->
        {
            LeftModRowController leftModRowController = this.leftModRowControllerFactory.Create(m);

            try
            {
                FXMLLoader fXMLLoader = leftModRowController.loadView("/views/ListModRowView.fxml");
                Parent view = fXMLLoader.getRoot();

                this.controllers.add(leftModRowController);

                this.executeOnUIThread(() ->
                {
                    this.listMod.getItems().add(view);
                });
            }
            catch (IOException ex)
            {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
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
