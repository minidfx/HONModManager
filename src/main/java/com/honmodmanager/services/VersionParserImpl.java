package com.honmodmanager.services;

import com.honmodmanager.models.VersionImpl;
import com.honmodmanager.models.contracts.Version;
import com.honmodmanager.services.contracts.VersionParser;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public final class VersionParserImpl implements VersionParser
{
    @Override
    public Version Parse(String version) throws ParseException
    {
        Pattern regex = Pattern.compile("(\\d+).?", Pattern.CASE_INSENSITIVE);
        Matcher match = regex.matcher(version);

        List<String> results = new ArrayList<>();

        while (match.find())
        {
            results.add(match.group(1));
        }

        final int listSize = results.size();

        if (listSize > 4)
            throw new ParseException("Cannot parse more than 4 integers to determine a version.", 0);

        if (listSize < 1)
            throw new ParseException("Cannot parse less than 1 integer to determine a version.", 0);

        int major = Integer.valueOf(results.get(0));
        int minor = listSize > 1 ? Integer.valueOf(results.get(1)) : 0;
        int fix = listSize > 2 ? Integer.valueOf(results.get(2)) : 0;
        int build = listSize > 3 ? Integer.valueOf(results.get(3)) : 0;

        return new VersionImpl(major, minor, fix, build);
    }
}
