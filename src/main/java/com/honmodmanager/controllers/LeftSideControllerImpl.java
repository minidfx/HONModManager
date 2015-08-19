package com.honmodmanager.controllers;

import com.honmodmanager.controllers.contracts.LeftSideController;
import java.net.URL;
import java.util.ResourceBundle;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * The controller for communicating wjth the view representing the mods
 * installed in the game.
 *
 * @author Burgy Benjamin
 */
@Scope("singleton")
@Service
public class LeftSideControllerImpl implements LeftSideController
{
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
