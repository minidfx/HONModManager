package com.honmodmanager.services;

import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.services.contracts.GameInformation;
import com.honmodmanager.services.contracts.ModManager;
import com.honmodmanager.services.contracts.ModReader;
import com.honmodmanager.services.contracts.ModWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import rx.Observable;

@Service
@Scope("singleton")
public final class ModManagerImpl implements ModManager
{
    private static final Logger LOG = Logger.getLogger(ModManagerImpl.class.getName());

    private final ModWriter modWriter;
    private final ModReader modReader;
    private final GameInformation gameInformation;

    @Autowired
    public ModManagerImpl(ModWriter modWriter,
                          ModReader modReader,
                          GameInformation gameInformation)
    {
        this.modWriter = modWriter;
        this.modReader = modReader;
        this.gameInformation = gameInformation;
    }

    @Override
    public Observable<Boolean> apply()
    {
        return this.modWriter.Write();
    }

    @Override
    public void add(Path path) throws IOException
    {
        File targetDirectory = this.gameInformation.getModsFolder().toFile();
        if (!targetDirectory.exists())
        {
            targetDirectory.mkdir();
        }

        File newMod = path.toFile();
        FileUtils.copyFileToDirectory(newMod, targetDirectory, true);
    }

    @Override
    public void remove(Mod mod)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Observable<Mod> getAll()
    {
        return this.modReader.getMods();
    }
}
