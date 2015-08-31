package com.honmodmanager.services;

import com.honmodmanager.models.VersionImpl;
import com.honmodmanager.models.contracts.Version;
import com.honmodmanager.services.contracts.OperatingSystemInformation;
import com.honmodmanager.services.contracts.GameInformation;
import com.honmodmanager.services.contracts.OperatingSystem;
import com.honmodmanager.services.contracts.VersionParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.Subscriber;

@Service
@Scope("prototype")
public final class GameInformationImpl implements GameInformation
{
    private final OperatingSystemInformation operatingSystemInformation;
    private final VersionParser versionParser;

    @Autowired
    public GameInformationImpl(OperatingSystemInformation determiningOperatingSystem,
                               VersionParser versionParser)
    {
        this.operatingSystemInformation = determiningOperatingSystem;
        this.versionParser = versionParser;
    }

    @Override
    public Path getFolderPath()
    {
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
    public Path getExecutablePath()
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

        return Paths.get(this.getFolderPath().toString(), executableName);
    }

    @Override
    @SuppressWarnings(
            {
                "empty-statement", "UseSpecificCatch"
            })
    public Observable<Version> getVersion()
    {
        return Observable.create((Subscriber<? super Version> observer) ->
        {
            try
            {
                File executable = this.getExecutablePath().toFile();

                if (!executable.exists())
                    throw new FileNotFoundException("Heroes of Newerth executable not found.");

                FileInputStream inputStream = new FileInputStream(executable);

                byte[] buffer = GameInformationImpl.ReadAllBytes(executable.length(), inputStream);

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

                Version version = this.versionParser.parse(new String(secondPass));

                observer.onNext(version);
                observer.onCompleted();
            }
            catch (Exception exception)
            {
                observer.onError(exception);
            }
        });
    }

    @Override
    public Version getAdditionalResourceVersion() throws FileNotFoundException,
                                                         ZipException,
                                                         IOException,
                                                         ParseException
    {
        File additionalResource = this.getAdditonalResourcePath().toFile();

        if (!additionalResource.exists())
            throw new FileNotFoundException("Additional resource doesn't exist.");

        ZipFile zip = new ZipFile(additionalResource);
        String zipComments = this.getZipComments(additionalResource);

        Pattern captureVersion = Pattern.compile("Game Version: ([\\.0-9]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = captureVersion.matcher(zipComments);

        if (!matcher.find())
            throw new ParseException("Cannot parse the version in the comments", 0);

        return this.versionParser.parse(matcher.group(1));
    }

    @Override
    public Path getPreferencesFolderPath()
    {
        switch (this.operatingSystemInformation.getOperatingSystem())
        {
            case Linux:
                return Paths.get(System.getenv("HOME"), "Heroes of Newerth/game");

            case MacOSx:
                return Paths.get(System.getenv("HOME"), "Library", "Application Support", "Heroes of Newerth", "game");

            case Windows:
                return Paths.get(this.getFolderPath().toString(), "game");

            default:
                throw new UnsupportedOperationException("Operating system unknown.");
        }
    }

    @Override
    public String getZipComments(File file) throws FileNotFoundException,
                                                   ZipException,
                                                   IOException
    {
        long fileLength = file.length();
        int padLeft = 22;

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = GameInformationImpl.ReadAllBytes(fileLength, fileInputStream);

        int pos = GameInformationImpl.indexOf(buffer, new byte[]
                                      {
                                          0x50, 0x4b, 0x05, 0x06
        });

        if (pos != -1)
        {

            int commentsLength = buffer.length - (pos + padLeft);
            return new String(buffer, pos + padLeft, commentsLength);
        }

        throw new ZipException("Zip comments not found.");
    }

    private static byte[] ReadAllBytes(long fileLength, FileInputStream fileInputStream) throws IOException
    {
        byte[] buffer = new byte[(int) fileLength];
        int offset = 0;
        int numRead = 0;

        while (offset < buffer.length && (numRead = fileInputStream.read(buffer, offset, buffer.length - offset)) >= 0)
        {
            offset += numRead;
        }

        return buffer;
    }

    /**
     * Search the data byte array for the first occurrence of the byte array
     * pattern. from
     *
     * @see
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
     *
     * @see
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

    @Override
    public Path getOriginalResourcePath()
    {
        return Paths.get(this.getFolderPath().toString(), "resources0.s2z");
    }

    @Override
    public Path getAdditonalResourcePath()
    {
        return Paths.get(this.getFolderPath().toString(), "resources999.s2z");
    }

    @Override
    public Path getModsFolder()
    {
        return Paths.get(this.getPreferencesFolderPath().toString(), "mods");
    }

    @Override
    public Version getModMangerVersion()
    {
        return new VersionImpl(1, 3, 6, 0);
    }
}
