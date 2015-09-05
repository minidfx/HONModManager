package com.honmodmanager.services;

import com.honmodmanager.models.RequirementImpl;
import com.honmodmanager.models.contracts.Requirement;
import com.honmodmanager.models.contracts.Version;
import com.honmodmanager.services.contracts.RequirementParser;
import com.honmodmanager.services.contracts.VersionParser;
import java.text.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public final class RequirementParserImpl implements RequirementParser
{
    private final VersionParser versionParser;

    @Autowired
    public RequirementParserImpl(VersionParser versionParser)
    {
        this.versionParser = versionParser;
    }

    @Override
    public Requirement parse(String requierement) throws ParseException
    {
        if (!requierement.contains("-"))
        {
            Version minimumVersion = this.versionParser.parse(requierement);
            return new RequirementImpl(minimumVersion);
        }

        String[] versions = requierement.split("-");
        Version mininumVersion = this.versionParser.parse(versions[0]);
        Version maximumVersion = this.versionParser.parse(versions[1]);

        return new RequirementImpl(mininumVersion, maximumVersion);
    }
}
