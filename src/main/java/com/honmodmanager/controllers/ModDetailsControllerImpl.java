package com.honmodmanager.controllers;

import com.honmodmanager.controllers.contracts.ModDetailsController;
import java.net.URL;
import java.util.ResourceBundle;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * The controller for communicating with the view containing the details of the
 * mod selected.
 *
 * @author Burgy Benjamin
 */
@Scope("singleton")
@Service
public class ModDetailsControllerImpl implements ModDetailsController
{
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
