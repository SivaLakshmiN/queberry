package io.queberry.que.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
//@AllArgsConstructor
@NoArgsConstructor(force = true,access = AccessLevel.PUBLIC)
public class QueueConfiguration extends AggregateRoot<QueueConfiguration> {

    @Enumerated(EnumType.STRING)
    private QueueStrategy strategy;


    private Integer slaWait;

    private Integer slaServe;

    @Enumerated(EnumType.STRING)
    private ServicePriority servicePriority;

    private String branchKey;

    public QueueConfiguration change(QueueStrategy queuingStrategy, Integer slaWait,Integer slaServe, ServicePriority servicePriority, Integer level){
        this.strategy = queuingStrategy;
        this.slaWait = slaWait;
        this.slaServe = slaServe;
        this.servicePriority = servicePriority;
        //this.level = level;
        return andEvent(ConfigurationEvent.of(this));
    }

    @Getter
    public enum QueueStrategy{
        FIFO,
        SERVICE_PRIORITY
    }

    @Getter
    public enum ServicePriority{
        COUNTER,
        USER,
        BOTH
    }

    public QueueConfiguration(QueueStrategy strategy, Integer slaWait, Integer slaServe, ServicePriority servicePriority) {
        this.strategy = strategy;
        this.slaWait = slaWait;
        this.slaServe = slaServe;
        this.servicePriority = servicePriority;
        registerEvent(ConfigurationEvent.of(this));
    }

    public QueueConfiguration(QueueStrategy strategy,Integer slaWait,Integer slaServe, ServicePriority servicePriority, String branchKey) {
        this.strategy = strategy;
        this.slaWait = slaWait;
        this.slaServe = slaServe;
        this.servicePriority = servicePriority;
        this.branchKey = branchKey;
        registerEvent(ConfigurationEvent.of(this));
    }


    @Override
    @JsonIgnore
    public String getId(){
        return super.getId();
    }

    @Override
    @JsonIgnore
    public Long getVersion(){
        return super.getVersion();
    }
}
