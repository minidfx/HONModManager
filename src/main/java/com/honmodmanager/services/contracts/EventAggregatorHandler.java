package com.honmodmanager.services.contracts;

/**
 *
 * @author Burgy Benjamin
 * @param <TEvent>
 */
public interface EventAggregatorHandler<TEvent>
{
    void handle(TEvent event);
}
