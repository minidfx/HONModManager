package com.honmodmanager.services;

import com.honmodmanager.services.contracts.OperatingSystemInformation;
import com.honmodmanager.services.contracts.OperatingSystem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class OperatingSystemInformationImpl implements OperatingSystemInformation
{
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
