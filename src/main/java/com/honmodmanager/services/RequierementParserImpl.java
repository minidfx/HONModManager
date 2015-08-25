package com.honmodmanager.services;

import com.honmodmanager.models.RequierementImpl;
import com.honmodmanager.models.contracts.Requierement;
import com.honmodmanager.models.contracts.Version;
import com.honmodmanager.services.contracts.RequierementParser;
import com.honmodmanager.services.contracts.VersionParser;
import java.text.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public final class RequierementParserImpl implements RequierementParser
{
    private final VersionParser versionParser;

    @Autowired
    public RequierementParserImpl(VersionParser versionParser)
    {
        this.versionParser = versionParser;
    }

    @Override
    public Requierement parse(String requierement) throws ParseException
    {
        if (!requierement.contains("-"))
        {
            Version minimumVersion = this.versionParser.parse(requierement);
            return new RequierementImpl(minimumVersion);
        }

        String[] versions = requierement.split("-");
        Version mininumVersion = this.versionParser.parse(versions[0]);
        Version maximumVersion = this.versionParser.parse(versions[1]);

        return new RequierementImpl(mininumVersion, maximumVersion);
    }
}
