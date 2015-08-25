package com.honmodmanager.storage;

import com.github.jlinqer.collections.List;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.storage.contracts.Storage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public final class MemoryStorage implements Storage
{
    private final List<Mod> mods;

    public MemoryStorage()
    {
        this.mods = new List<>();
    }

    @Override
    public List<Mod> getMods()
    {
        return this.mods;
    }

    @Override
    public void addMod(Mod mod)
    {
        this.mods.add(mod);
    }

    @Override
    public void clear()
    {
        this.mods.clear();
    }
}
