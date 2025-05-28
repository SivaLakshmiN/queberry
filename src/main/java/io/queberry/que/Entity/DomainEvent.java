package io.queberry.que.Entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.w3c.dom.events.Event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author firoz
 */

@Getter
@EqualsAndHashCode(of = "id")
public abstract class DomainEvent<T> implements Event {

    private final UUID id = UUID.randomUUID();
    private final LocalDateTime occured = LocalDateTime.now();

    protected DomainEvent() {

    }
}
