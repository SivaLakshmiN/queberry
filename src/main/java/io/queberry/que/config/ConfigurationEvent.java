package io.queberry.que.config;
import io.queberry.que.anotation.AggregateReference;
import io.queberry.que.entity.AggregateRoot;
import io.queberry.que.entity.DomainEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.w3c.dom.events.EventTarget;

@Value
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor
public class ConfigurationEvent extends DomainEvent {

    @AggregateReference
    private final AggregateRoot aggregateRoot;

    public static ConfigurationEvent of(AggregateRoot aggregateRoot) {
        return new ConfigurationEvent(aggregateRoot);
    }
}
