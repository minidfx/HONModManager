package com.honmodmanager.services;

import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.services.contracts.ModReader;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public final class ModReaderImpl implements ModReader
{
    @Override
    public List<Mod> getInstalledMods()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
