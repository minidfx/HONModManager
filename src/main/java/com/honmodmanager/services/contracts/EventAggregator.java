package com.honmodmanager.services.contracts;

/**
 *
 * @author Burgy Benjamin
 */
public interface EventAggregator
{
    void Publish(Object o);

    <TEvent, TSubscriber extends EventAggregatorHandler<TEvent>> void Subscribe(TSubscriber subscriber);

    <TSubscriber> void unsubscribe(TSubscriber subscriber);
}
