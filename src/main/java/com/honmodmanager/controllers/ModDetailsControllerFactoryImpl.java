package com.honmodmanager.controllers;

import com.honmodmanager.controllers.contracts.ModDetailsController;
import com.honmodmanager.controllers.contracts.ModDetailsControllerFactory;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.services.contracts.EventAggregator;
import com.honmodmanager.services.contracts.PlatformInteraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class ModDetailsControllerFactoryImpl implements ModDetailsControllerFactory
{
    private final PlatformInteraction platformInteraction;
    private final EventAggregator eventAggregator;

    @Autowired
    public ModDetailsControllerFactoryImpl(PlatformInteraction platformInteraction,
                                           EventAggregator eventAggregator)
    {
        this.platformInteraction = platformInteraction;
        this.eventAggregator = eventAggregator;
    }

    @Override
    public ModDetailsController Create(Mod model)
    {
        return new ModDetailsControllerImpl(model,
                                            this.platformInteraction,
                                            this.eventAggregator);
    }
}
