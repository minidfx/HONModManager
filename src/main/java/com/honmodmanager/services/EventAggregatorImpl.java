package com.honmodmanager.services;

import com.github.jlinqer.collections.Dictionary;
import com.github.jlinqer.collections.List;
import com.github.jlinqer.linq.IEnumerable;
import com.honmodmanager.services.contracts.EventAggregator;
import com.honmodmanager.services.contracts.EventAggregatorHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public final class EventAggregatorImpl implements EventAggregator
{
    private final Dictionary<Type, EventAggregatorHandler> subscribers;

    public EventAggregatorImpl()
    {
        this.subscribers = new Dictionary<>();
    }

    @Override
    public void Publish(Object o)
    {
        Type eventType = o.getClass();

        IEnumerable<EventAggregatorHandler> filteredSubscribers = this.subscribers.where(x ->
                x.getKey().equals(eventType)).select(x -> x.getValue());

        for (EventAggregatorHandler subscriber : filteredSubscribers)
        {
            subscriber.handle(o);
        }
    }

    private Type[] getHandlerEventType(Object o)
    {
        IEnumerable<ParameterizedType> eventAggregatorGenericTypes = new List(Arrays.asList(o.getClass().getGenericInterfaces())).where((
                Object x) ->
                x instanceof ParameterizedType);

        IEnumerable<ParameterizedType> eventAggregatorInterfaces = eventAggregatorGenericTypes.where(x ->
                x.getRawType().equals(EventAggregatorHandler.class));

        IEnumerable<Type> eventTypes = eventAggregatorInterfaces.selectMany(x ->
                new List(Arrays.asList(x.getActualTypeArguments())));

        return (Type[]) eventTypes.toArray(Type.class);
    }

    @Override
    public <TEvent, TSubscriber extends EventAggregatorHandler<TEvent>> void Subscribe(TSubscriber subscriber)
    {
        if (!this.subscribers.any(x -> x.getValue().equals(subscriber)))
        {
            Type[] eventTypes = this.getHandlerEventType(subscriber);

            for (Type eventType : eventTypes)
            {
                this.subscribers.put(eventType, subscriber);
            }
        }
    }
}
