package com.honmodmanager.services;

import com.honmodmanager.services.contracts.ConnectionTester;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import rx.Observable;

@Service
@Scope("singleton")
public final class ConnectionTesterImpl implements ConnectionTester
{
    private static final Logger LOG = Logger.getLogger(ConnectionTesterImpl.class.getName());

    @Override
    public Observable<Boolean> TestUrl(URI uri)
    {
        return Observable.create(subscriber ->
        {
            try
            {
                HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();

                try
                {
                    int responseCode = connection.getResponseCode();
                    int firstDigitResponseCode = responseCode / 100;

                    subscriber.onNext(firstDigitResponseCode == 2 || firstDigitResponseCode == 3);
                    subscriber.onCompleted();
                }
                finally
                {
                    connection.disconnect();
                }
            }
            catch (Exception ex)
            {
                LOG.log(Level.WARNING, ex.getMessage(), ex);
                subscriber.onError(ex);
            }
        });
    }
}
