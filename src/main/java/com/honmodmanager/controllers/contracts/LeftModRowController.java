package com.honmodmanager.controllers.contracts;

import com.honmodmanager.models.contracts.Mod;

/**
 *
 * @author Burgy Benjamin
 */
public interface LeftModRowController extends FXmlController
{
    /**
     * Returns the model containing information to build the view associated to
     * this controller.
     *
     * @return
     */
    Mod getModel();

    void refresh();
}
