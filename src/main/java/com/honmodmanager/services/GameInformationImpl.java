package com.honmodmanager.services;

import com.honmodmanager.models.VersionImpl;
import com.honmodmanager.models.contracts.Version;
import com.honmodmanager.services.contracts.OperatingSystemInformation;
import com.honmodmanager.services.contracts.GameInformation;
import com.honmodmanager.services.contracts.OperatingSystem;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public final class GameInformationImpl implements GameInformation
{
    private final OperatingSystemInformation operatingSystemInformation;

    public GameInformationImpl(OperatingSystemInformation determiningOperatingSystem)
    {
        this.operatingSystemInformation = determiningOperatingSystem;
    }

    @Override
    public Path getFolder()
    {
        String gamePath;

        switch (this.operatingSystemInformation.getOperatingSystem())
        {
            case Windows:
                return Paths.get(System.getenv("ProgramFiles(x86)"), "Heroes of Newerth");

            case MacOSx:
                return Paths.get("/Applications/Heroes of Newerth.app");

            case Linux:
                return Paths.get(System.getenv("HOME"), "HoN");

            default:
                throw new UnsupportedOperationException("Operating system unknown.");
        }
    }

    @Override
    public Path getExecutable()
    {
        String executableName;

        switch (this.operatingSystemInformation.getOperatingSystem())
        {
            case Linux:
                executableName = "hon-x86";
                break;

            case MacOSx:
                executableName = "HoN";
                break;

            case Windows:
                executableName = "hon.exe";
                break;

            default:
                throw new UnsupportedOperationException("Operating system unknown.");
        }

        return Paths.get(this.getFolder().toString(), executableName);
    }

    @Override
    @SuppressWarnings("empty-statement")
    public Version getVersion() throws FileNotFoundException, ParseException, IOException
    {
        File executable = this.getExecutable().toFile();

        if (!executable.exists())
            throw new FileNotFoundException("Heroes of Newerth executable not found.");

        FileInputStream inputStream = new FileInputStream(executable);

        byte[] buffer = new byte[(int) executable.length()];
        int offset = 0;
        int numRead = 0;

        // Read bytes
        while (offset < buffer.length && (numRead = inputStream.read(buffer, offset, buffer.length - offset)) >= 0)
        {
            offset += numRead;
        }

        // Reset the stream position
        int startPosition = 0;

        final OperatingSystem operatingSystem = this.operatingSystemInformation.getOperatingSystem();

        switch (operatingSystem)
        {
            case Linux:
            case MacOSx:
                startPosition = GameInformationImpl.indexOf(buffer, new byte[]
                                                    {
                                                        0x5b, 0, 0, 0, 0x55, 0, 0, 0, 0x4e, 0, 0, 0, 0x49, 0, 0, 0, 0x43, 0, 0, 0, 0x4f, 0, 0, 0, 0x44, 0, 0, 0, 0x45, 0, 0, 0, 0x5d
                });
                startPosition += 40;
                break;

            case Windows:
                startPosition = GameInformationImpl.indexOf(buffer, new byte[]
                                                    {
                                                        0x43, 0, 0x55, 0, 0x52, 0, 0x45, 0, 0x20, 0, 0x43, 0, 0x52, 0, 0x54, 0, 0x5d
                });
                startPosition += 20;
                break;

            default:
                throw new UnsupportedOperationException("Operating system unknown.");
        }

        char[] firstPass = new char[33];
        char[] secondPass = new char[8];

        for (int i = startPosition; i < startPosition + 33; firstPass[i - startPosition] = (char) buffer[i++]);

        int j = 0;
        for (char c : firstPass)
        {
            if ((int) c > 0 && j < secondPass.length)
            {
                secondPass[j++] = c;
            }
        }

        Pattern pVersion = Pattern.compile("^(\\d*)\\.(\\d*)\\.(\\d*)\\.(\\d*).*$", Pattern.CASE_INSENSITIVE);
        Matcher match = pVersion.matcher(new String(secondPass));

        if (!match.matches())
            throw new ParseException("Unable to identify the version.", 0);

        Integer major = Integer.valueOf(match.group(1));
        Integer minor = Integer.valueOf(match.group(2));
        Integer fix = Integer.valueOf(match.group(3));
        Integer build = Integer.valueOf(match.group(4));

        return new VersionImpl(major, minor, fix, build);
    }

    @Override
    public Version getOriginalResourceVersion()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Path getPreferencesFolder()
    {
        switch (this.operatingSystemInformation.getOperatingSystem())
        {
            case Linux:
                return Paths.get(System.getenv("HOME"), "Heroes of Newerth/game");

            case MacOSx:
                return Paths.get(System.getenv("HOME"), "Library/Application Support/", "Heroes of Newerth/game");

            case Windows:
                return Paths.get(this.getFolder().toString(), "game");

            default:
                throw new UnsupportedOperationException("Operating system unknown.");
        }
    }

    @Override
    public String getZipComments()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Search the data byte array for the first occurrence of the byte array
     * pattern. from
     * http://helpdesk.objects.com.au/java/search-a-byte-array-for-a-byte-sequence
     */
    private static int indexOf(byte[] data, byte[] pattern)
    {
        int[] failure = GameInformationImpl.computeFailure(pattern);

        int j = 0;

        for (int i = 0; i < data.length; i++)
        {

            while (j > 0 && pattern[j] != data[i])
            {
                j = failure[j - 1];
            }

            if (pattern[j] == data[i])
            {
                j++;
            }

            if (j == pattern.length)
            {
                return i - pattern.length + 1;
            }
        }

        return -1;
    }

    /**
     * Computes the failure function using a boot-strapping process, where the
     * pattern is matched against itself. from
     * http://helpdesk.objects.com.au/java/search-a-byte-array-for-a-byte-sequence
     */
    private static int[] computeFailure(byte[] pattern)
    {
        int[] failure = new int[pattern.length];

        int j = 0;
        for (int i = 1; i < pattern.length; i++)
        {

            while (j > 0 && pattern[j] != pattern[i])
            {
                j = failure[j - 1];
            }

            if (pattern[j] == pattern[i])
            {
                j++;
            }

            failure[i] = j;
        }

        return failure;
    }
}
