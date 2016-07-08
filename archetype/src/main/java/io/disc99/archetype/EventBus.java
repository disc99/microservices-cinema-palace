package io.disc99.archetype;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class EventBus {

    private Set<EventHandler> holder;

    public EventBus() {
        this.holder = new HashSet<>();
    }


    public void register(EventHandler handler) {
        holder.add(handler);
    }


    @SuppressWarnings("unchecked")
    public void dispatch(DomainEvent event) {
        Predicate<EventHandler> byEvent = handler -> Arrays.stream(handler.getClass().getGenericInterfaces())
                .filter(ParameterizedType.class::isInstance)
                .anyMatch(type -> ParameterizedType.class.cast(type).getActualTypeArguments()[0] == event.getClass());

        holder.stream()
                .filter(byEvent)
                .forEach(handler -> handler.handle(event));
    }
}