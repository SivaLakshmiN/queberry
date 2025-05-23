package io.queberry.que.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;

//import javax.persistence.Id;
//import javax.persistence.MappedSuperclass;
//import javax.persistence.Version;
import java.util.UUID;

/**
 * @author firoz
 * @since 17/09/17
 */

@Getter
@MappedSuperclass
@EqualsAndHashCode(of = "id")
//@EntityListeners(LifecycleEventPublisher.class)
public abstract class AggregateRoot<A extends AbstractAggregateRoot<A>> extends AbstractAggregateRoot<A> {

    @Id
    private String id = UUID.randomUUID().toString();

    @Version
    private Long version = null;
}
