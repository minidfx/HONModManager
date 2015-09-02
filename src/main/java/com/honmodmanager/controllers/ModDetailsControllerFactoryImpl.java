package com.honmodmanager.controllers;

import com.honmodmanager.controllers.contracts.ModDetailsController;
import com.honmodmanager.controllers.contracts.ModDetailsControllerFactory;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.services.contracts.ConnectionTester;
import com.honmodmanager.services.contracts.EventAggregator;
import com.honmodmanager.services.contracts.ModEnabler;
import com.honmodmanager.services.contracts.ModUpdater;
import com.honmodmanager.services.contracts.PlatformInteraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public final class ModDetailsControllerFactoryImpl implements ModDetailsControllerFactory
{
    private final PlatformInteraction platformInteraction;
    private final EventAggregator eventAggregator;
    private final ModUpdater modUpdater;
    private final ModEnabler modEnabler;
    private final ConnectionTester connectionTester;

    @Autowired
    public ModDetailsControllerFactoryImpl(PlatformInteraction platformInteraction,
                                           EventAggregator eventAggregator,
                                           ModUpdater modUpdater,
                                           ModEnabler modEnabler,
                                           ConnectionTester connectionTester)
    {
        this.platformInteraction = platformInteraction;
        this.eventAggregator = eventAggregator;
        this.modUpdater = modUpdater;
        this.modEnabler = modEnabler;
        this.connectionTester = connectionTester;
    }

    @Override
    public ModDetailsController Create(Mod model)
    {
        return new ModDetailsControllerImpl(model,
                                            this.platformInteraction,
                                            this.eventAggregator,
                                            this.modUpdater,
                                            this.modEnabler,
                                            this.connectionTester);
    }
}
