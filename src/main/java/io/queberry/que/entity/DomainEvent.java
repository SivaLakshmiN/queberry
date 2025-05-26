package io.queberry.que.entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
public abstract class DomainEvent<T> implements Event {

    private final UUID id = UUID.randomUUID();
    private final LocalDateTime occured = LocalDateTime.now();

    protected DomainEvent() {

    }
}

