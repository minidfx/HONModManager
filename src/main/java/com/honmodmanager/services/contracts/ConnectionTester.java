package com.honmodmanager.services.contracts;

import java.net.URI;
import rx.Observable;

/**
 *
 * @author Burgy Benjamin
 */
public interface ConnectionTester
{
    Observable<Boolean> TestUrl(URI uri);
}
