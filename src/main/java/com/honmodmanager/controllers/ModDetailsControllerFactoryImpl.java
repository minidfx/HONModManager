package com.honmodmanager.controllers;

import com.honmodmanager.controllers.contracts.ModDetailsController;
import com.honmodmanager.controllers.contracts.ModDetailsControllerFactory;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.services.contracts.PlatformInteraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class ModDetailsControllerFactoryImpl implements ModDetailsControllerFactory
{
    private final PlatformInteraction platformInteraction;

    @Autowired
    public ModDetailsControllerFactoryImpl(PlatformInteraction platformInteraction)
    {
        this.platformInteraction = platformInteraction;
    }

    @Override
    public ModDetailsController Create(Mod model)
    {
        return new ModDetailsControllerImpl(model, this.platformInteraction);
    }
}
