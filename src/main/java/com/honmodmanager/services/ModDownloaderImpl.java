package com.honmodmanager.services;

import com.honmodmanager.services.contracts.ModDownloader;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import rx.Observable;

/**
 *
 * @author Burgy Benjamin
 */
@Service
@Scope("prototype")
public final class ModDownloaderImpl implements ModDownloader
{
    @Override
    public Observable<File> Download(URI uri, String destination) throws MalformedURLException, IOException
    {
        throw new UnsupportedOperationException("Not implemented yet.");
        //        try
        //        {
        //            URL url = uri.toURL();
        //            File downloadedFile = new File(destination);
        //
        //            try
        //            {
        //                ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        //                FileOutputStream fos = new FileOutputStream(downloadedFile);
        //                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        //
        //                obsrvr.onNext(downloadedFile);
        //                obsrvr.onCompleted();
        //            }
        //            finally
        //            {
        //                downloadedFile.deleteOnExit();
        //            }
        //        }
        //        catch (Exception ex)
        //        {
        //            obsrvr.onError(ex);
        //        }
    }
}
