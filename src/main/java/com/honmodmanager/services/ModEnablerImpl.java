package com.honmodmanager.services;

import com.github.jlinqer.collections.List;
import com.honmodmanager.exceptions.ModActivationException;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.models.contracts.Requierement;
import com.honmodmanager.models.contracts.Version;
import com.honmodmanager.services.contracts.GameInformation;
import com.honmodmanager.services.contracts.ModEnabler;
import com.honmodmanager.services.contracts.ModReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public final class ModEnablerImpl implements ModEnabler
{
    private final GameInformation gameInformation;
    private final ModReader modReader;

    @Autowired
    public ModEnablerImpl(GameInformation gameInformation, ModReader modReader)
    {
        this.gameInformation = gameInformation;
        this.modReader = modReader;
    }

    @Override
    public void enable(Mod mod) throws ModActivationException
    {
        List<String> requierements = mod.getRequiredMods();
        List<String> incompatibillities = mod.getIncompatibillities();
        List<Mod> mods = new List(modReader.getMods().toBlockingObservable().toIterable());
        Version gameVersion = this.gameInformation.getVersion().toBlockingObservable().first();
        Requierement modGameVersion = mod.getGameVersion();

        boolean hasRequierement = !requierements.any() || requierements.any(x ->
        {
            Mod requiredMod = mods.singleOrDefault(m -> m.getId().equals(x));
            return requiredMod != null && mod.isEnabled();
        });

        if (!hasRequierement)
        {
            throw new ModActivationException(String.format("The mod %s doesn't have the minimum requierement.", mod.getName()));
        }

        boolean hasIncompatibilities = incompatibillities.any(x ->
        {
            return mods.any(m -> m.getId().equals(x));
        });

        if (hasIncompatibilities)
        {
            throw new ModActivationException("There is an incompatible mod.");
        }

        Version modMinimumVersion = modGameVersion.getMininum();
        Version modMaximumVersion = modGameVersion.getMaximum();

        if (!modMinimumVersion.isNull() && gameVersion.lowerThan(modMinimumVersion))
        {
            throw new ModActivationException("Your game is outdated, please update it and try again.");
        }

        if (!modMaximumVersion.isNull() && gameVersion.greaterThan(modGameVersion.getMaximum()))
        {
            throw new ModActivationException("Your mod is outdated, please update it and try again.");
        }

        mod.enabled(true);
    }

    @Override
    public void disable(Mod mod)
    {
        mod.enabled(false);
    }
}
