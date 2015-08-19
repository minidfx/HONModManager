package com.honmodmanager.services;

import com.github.jlinqer.collections.List;
import com.honmodmanager.exceptions.MalformedZipCommentsException;
import com.honmodmanager.models.ModImpl;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.models.contracts.Version;
import com.honmodmanager.services.contracts.GameInformation;
import com.honmodmanager.services.contracts.ModReader;
import com.honmodmanager.services.contracts.VersionParser;
import java.io.File;
import java.text.ParseException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

@Service
@Scope("prototype")
public final class ModReaderImpl implements ModReader
{
    private static final Logger LOG = Logger.getLogger(ModReaderImpl.class.getName());

    private final GameInformation gameInformation;
    private final VersionParser versionParser;

    @Autowired
    public ModReaderImpl(GameInformation gameInformation, VersionParser versionParser)
    {
        this.gameInformation = gameInformation;
        this.versionParser = versionParser;
    }

    @Override
    @SuppressWarnings("UseSpecificCatch")
    public Observable<Mod> getMods()
    {
        return Observable.create((Subscriber<? super Mod> observer) ->
        {
            try
            {
                File additionalResourceFile = this.gameInformation.getAdditonalResourcePath().toFile();

                if (!additionalResourceFile.exists())
                {
                    observer.onCompleted();
                    return;
                }

                String zipComments = this.gameInformation.getZipComments(additionalResourceFile);

                if (!zipComments.startsWith("HoN Mod Manager v"))
                    throw new MalformedZipCommentsException();

                List<String> linesComments = new List<>(zipComments.split("\n"))
                        .skipWhile(s ->
                                {
                                    return !s.equals("Applied Mods:");
                        })
                        .toList();

                for (String line : linesComments)
                {
                    Mod newMod = this.initMod(line);

                    LOG.info(String.format("Mod installed: %s (%s)", newMod.getName(), newMod.getVersion()));

                    observer.onNext(newMod);
                }

                observer.onCompleted();
            }
            catch (Exception e)
            {
                observer.onError(e);
            }
        })
                .subscribeOn(Schedulers.io());
    }

    private Mod initMod(String line) throws ParseException
    {
        Pattern parseModName = Pattern.compile("^(.+).*\\(v(.+)\\)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = parseModName.matcher(line.replace("\r", "").replace("\n", ""));

        if (!matcher.matches())
            throw new ParseException("Cannot parse the mod installed.", 0);

        String name = matcher.group(1);
        Version version = this.versionParser.Parse(matcher.group(2));

        return new ModImpl(line, version);
    }
}
