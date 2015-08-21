package com.honmodmanager.services.contracts;

import java.io.IOException;
import javafx.stage.Stage;

/**
 *
 * @author Burgy Benjamin
 */
public interface Bootstrapper
{
    void run(Stage stage) throws IOException;
}
