package com.honmodmanager.services;

import com.github.jlinqer.collections.List;
import com.github.jlinqer.linq.IEnumerable;
import com.honmodmanager.exceptions.ModActivationException;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.models.contracts.ModRequirement;
import com.honmodmanager.models.contracts.Requirement;
import com.honmodmanager.models.contracts.Version;
import com.honmodmanager.services.contracts.GameInformation;
import com.honmodmanager.services.contracts.ModEnabler;
import com.honmodmanager.services.contracts.ModReader;
import com.honmodmanager.services.contracts.RequirementValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public final class ModEnablerImpl implements ModEnabler
{
    private final GameInformation gameInformation;
    private final ModReader modReader;
    private final RequirementValidation requirementValidation;

    @Autowired
    public ModEnablerImpl(GameInformation gameInformation,
                          ModReader modReader,
                          RequirementValidation requirementValidation)
    {
        this.gameInformation = gameInformation;
        this.modReader = modReader;
        this.requirementValidation = requirementValidation;
    }

    @Override
    public void enable(Mod mod) throws ModActivationException
    {
        List<ModRequirement> requirements = mod.getRequiredMods();
        List<String> incompatibillities = mod.getIncompatibillities();
        List<Mod> mods = modReader.getCachedMods();
        Version gameVersion = this.gameInformation.getVersion().toBlockingObservable().first();
        Requirement modGameVersion = mod.getGameVersion();

        boolean hasRequirement = !requirements.any() || requirements.any(x ->
        {
            Mod requiredMod = mods.singleOrDefault(m ->
                    m.getId().equals(x.getModId()));

            return requiredMod != null && requiredMod.isEnabled() && this.requirementValidation.isValid(mod.getVersion(), x.getRequirement());
        });

        if (!hasRequirement)
        {
            throw new ModActivationException(String.format("The mod %s doesn't have the minimum requirement.", mod.getName()));
        }

        boolean hasIncompatibilities = incompatibillities.any(x ->
        {
            return mods.any(m -> m.getId().equals(x) && m.isEnabled());
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

        List<Mod> availableMods = this.modReader.getCachedMods();

        IEnumerable<Mod> dependingOf = availableMods.where(x ->
                x.getRequiredMods().any(m -> m.getModId().equals(m.getModId())));

        for (Mod depends : dependingOf)
        {
            depends.enabled(false);
        }
    }
}
