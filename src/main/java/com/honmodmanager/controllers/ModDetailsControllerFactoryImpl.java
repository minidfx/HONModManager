package com.honmodmanager.controllers;

import com.honmodmanager.controllers.contracts.ModDetailsController;
import com.honmodmanager.controllers.contracts.ModDetailsControllerFactory;
import com.honmodmanager.models.contracts.Mod;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class ModDetailsControllerFactoryImpl implements ModDetailsControllerFactory
{
    @Override
    public ModDetailsController Create(Mod model)
    {
        return new ModDetailsControllerImpl(model);
    }
}
