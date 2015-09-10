package com.honmodmanager.services;

import com.honmodmanager.services.contracts.OperatingSystem;
import com.honmodmanager.services.contracts.OperatingSystemInformation;
import java.util.logging.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public final class OperatingSystemInformationImpl implements OperatingSystemInformation
{
    private static final Logger LOG = Logger.getLogger(OperatingSystemInformationImpl.class.getName());

    private OperatingSystem operatingSystem;

    @Override
    public OperatingSystem getOperatingSystem()
    {
        if (this.operatingSystem != null)
        {
            return this.operatingSystem;
        }

        String operatingSystemName = System.getProperty("os.name").trim();

        if (operatingSystemName.equals("Mac OS X"))
            return this.operatingSystem = OperatingSystem.MacOSx;

        if (operatingSystemName.startsWith("Windows"))
            return this.operatingSystem = OperatingSystem.Windows;

        if (operatingSystemName.contains("nix") || operatingSystemName.contains("nux"))
            return this.operatingSystem = OperatingSystem.Linux;

        throw new UnsupportedOperationException("Cannot determines the operating system.");
    }

    @Override
    public boolean isX64()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
