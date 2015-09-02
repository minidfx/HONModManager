package com.honmodmanager.services;

import com.honmodmanager.services.contracts.OperatingSystemInformation;
import com.honmodmanager.services.contracts.OperatingSystem;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.schedulers.Schedulers;

@Service
@Scope("singleton")
public final class OperatingSystemInformationImpl implements OperatingSystemInformation
{
    private static final Logger LOG = Logger.getLogger(OperatingSystemInformationImpl.class.getName());

    private OperatingSystem operatingSystem;

    @Override
    public OperatingSystem getOperatingSystem()
    {
        if (this.operatingSystem == null)
        {
            String operatingSystemName = System.getProperty("os.name").trim();

            if (operatingSystemName.equals("Mac OS X"))
                this.operatingSystem = OperatingSystem.MacOSx;

            if (operatingSystemName.equals("Windows"))
                this.operatingSystem = OperatingSystem.Windows;

            if (operatingSystemName.contains("nix") || operatingSystemName.contains("nux"))
                this.operatingSystem = OperatingSystem.Linux;
        }

        return this.operatingSystem;
    }

    @Override
    public boolean isX64()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
