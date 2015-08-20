package com.honmodmanager.controllers;

import com.honmodmanager.controllers.contracts.LeftModRowController;
import com.honmodmanager.controllers.contracts.LeftModRowControllerFactory;
import com.honmodmanager.models.contracts.Mod;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public final class LeftModRowControllerFactoryImpl implements LeftModRowControllerFactory
{
    @Override
    public LeftModRowController Create(Mod mod)
    {
        return new LeftModRowControllerImpl(mod);
    }
}
