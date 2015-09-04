package com.honmodmanager;

import com.honmodmanager.contracts.ApplicationState;
import javafx.stage.Stage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author Burgy Benjamin
 */
@Service
@Scope("singleton")
public final class ModManagerState implements ApplicationState
{
    private Stage primaryStage;

    @Override
    public Stage getPrimaryStage()
    {
        return this.primaryStage;
    }

    @Override
    public void setPrimaryStage(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
    }
}
