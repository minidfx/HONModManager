package com.honmodmanager.services;

import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.services.contracts.ModReader;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

@Service
@Scope("prototype")
public final class ModReaderImpl implements ModReader
{
    @Override
    public Observable<Mod> getInstalledMods()
    {
        return Observable.create((Subscriber<? super Mod> observer) ->
        {
            try
            {
                Thread.sleep(10000);

                if (!observer.isUnsubscribed())
                {
                    observer.onCompleted();
                }
            }
            catch (Exception e)
            {
                observer.onError(e);
            }
        })
                .subscribeOn(Schedulers.io());
    }
}
