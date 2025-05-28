package io.queberry.que.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.w3c.dom.events.EventTarget;


@Value
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor
public class ConfigurationEvent extends DomainEvent {

    @io.queberry.que.Entity.AggregateReference
    private final AggregateRoot aggregateRoot;

    public static ConfigurationEvent of(AggregateRoot aggregateRoot) {
        return new ConfigurationEvent(aggregateRoot);
    }

    @Override
    public String getType() {
        return "";
    }

    @Override
    public EventTarget getTarget() {
        return null;
    }

    @Override
    public EventTarget getCurrentTarget() {
        return null;
    }

    @Override
    public short getEventPhase() {
        return 0;
    }

    @Override
    public boolean getBubbles() {
        return false;
    }

    @Override
    public boolean getCancelable() {
        return false;
    }

    @Override
    public long getTimeStamp() {
        return 0;
    }

    @Override
    public void stopPropagation() {

    }

    @Override
    public void preventDefault() {

    }

    @Override
    public void initEvent(String eventTypeArg, boolean canBubbleArg, boolean cancelableArg) {

    }
}
