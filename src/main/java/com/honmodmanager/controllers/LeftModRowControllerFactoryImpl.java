package com.honmodmanager.controllers;

import com.honmodmanager.controllers.contracts.LeftModRowController;
import com.honmodmanager.controllers.contracts.LeftModRowControllerFactory;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.services.contracts.EventAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public final class LeftModRowControllerFactoryImpl implements LeftModRowControllerFactory
{
    private final EventAggregator eventAggregator;

    @Autowired
    public LeftModRowControllerFactoryImpl(EventAggregator eventAggregator)
    {
        this.eventAggregator = eventAggregator;
    }

    @Override
    public LeftModRowController Create(Mod mod)
    {
        return new LeftModRowControllerImpl(mod, this.eventAggregator);
    }
}
