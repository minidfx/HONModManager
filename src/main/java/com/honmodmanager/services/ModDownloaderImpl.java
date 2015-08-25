package com.honmodmanager.services;

import com.github.jlinqer.collections.List;
import com.honmodmanager.services.contracts.ModDownloader;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author Burgy Benjamin
 */
@Service
@Scope("prototype")
public final class ModDownloaderImpl implements ModDownloader
{
    @Override
    public void download(URI uri, File destination) throws MalformedURLException,
                                                           FileNotFoundException,
                                                           IOException
    {
        URL url = uri.toURL();
        byte[] bytes = this.download(url);

        if (destination.exists())
            destination.delete();

        try (FileOutputStream fos = new FileOutputStream(destination))
        {
            fos.write(bytes);
        }
    }

    @Override
    public byte[] download(URL url) throws MalformedURLException,
                                           FileNotFoundException,
                                           IOException
    {
        List<HttpURLConnection> httpConnections = new List<>();

        try
        {
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setInstanceFollowRedirects(true);
            httpConnection.connect();

            httpConnections.add(httpConnection);

            while ((httpConnection.getResponseCode() / 100) == 3)
            {
                String newLocation = httpConnection.getHeaderField("Location");
                URL newUrl = new URL(newLocation);

                httpConnection = (HttpURLConnection) newUrl.openConnection();
                httpConnection.setInstanceFollowRedirects(true);
                httpConnection.connect();

                httpConnections.add(httpConnection);
            }

            try (BufferedInputStream bis = new BufferedInputStream(httpConnection.getInputStream()))
            {
                try (ByteOutputStream bos = new ByteOutputStream())
                {
                    final byte[] buffer = new byte[1024];
                    int read = 0;

                    while ((read = bis.read(buffer)) > 0)
                    {
                        bos.write(buffer);
                    }

                    return bos.getBytes();
                }
            }
        }
        finally
        {
            for (HttpURLConnection httpConnection : httpConnections)
            {
                httpConnection.disconnect();
            }
        }

    }
}
