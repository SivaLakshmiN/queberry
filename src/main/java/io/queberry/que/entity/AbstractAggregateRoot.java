package io.queberry.que.entity;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author firoz
 * @since 25/09/17
 */
public class AbstractAggregateRoot<A extends AbstractAggregateRoot<A>> implements Serializable {
    @Transient
    private transient final List<Object> domainEvents = new ArrayList<>();

    protected <T> void registerEvent(T event) {

        Assert.notNull(event, "Domain event must not be null!");

        this.domainEvents.add(event);
    }

    @AfterDomainEventPublication
    protected void clearDomainEvents() {
        this.domainEvents.clear();
    }

    @DomainEvents
    protected Collection<Object> domainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    protected final A andEventsFrom(A aggregate) {

        Assert.notNull(aggregate, "Aggregate must not be null!");

        this.domainEvents.addAll(aggregate.domainEvents());

        return (A) this;
    }

    protected final A andEvent(Object event) {

        registerEvent(event);

        return (A) this;
    }

}

