package com.honmodmanager.services.contracts;

import java.lang.reflect.Type;

/**
 *
 * @author Burgy Benjamin
 */
public interface EventAggregator
{
    /**
     * Publishes an event to subscribed objects.
     *
     * @param o
     */
    void publish(Object o);

    /**
     * Subscribes to listen publication of a specified type.
     *
     * @param <TEvent>
     * @param <TSubscriber>
     * @param subscriber
     */
    <TEvent, TSubscriber extends EventAggregatorHandler<TEvent>> void subscribe(TSubscriber subscriber);

    /**
     * Removes the subscription done to the EventAggregator.
     *
     * @param <TSubscriber>
     * @param subscriber
     */
    <TSubscriber> void unsubscribe(TSubscriber subscriber);
}
