package io.queberry.que.counter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.queberry.que.anotation.AggregateReference;
import io.queberry.que.entity.AggregateRoot;
import io.queberry.que.entity.DomainEvent;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Entity(name = "que_counter")
@Table(name = "que_counter", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"code", "branch_id"})
})
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "CounterCache")
public class Counter extends AggregateRoot<Counter> {

    @NotNull(message = "Counter name is mandatory!!")
    @Column(columnDefinition = "nvarchar(255)")
    private String name;

    @Column(columnDefinition = "nvarchar(255)", nullable = false)
    @NotNull(message = "Display Name is mandatory.")
    private String displayName;

    @NotNull(message = "Counter description is mandatory!!")
    @Column(columnDefinition = "nvarchar(255)")
    private String description;

    @Column(columnDefinition = "nvarchar(255)")
    @NotNull(message = "Counter code is mandatory!!")
    private String code;

    @Column(columnDefinition = "bit default 1")
    private boolean active = true;

    @Enumerated(EnumType.STRING)
    @NotNull()
    private Type type;

    @Column(columnDefinition = "bit default 0")
    private boolean inUse = false;

    private String colorCode;
    private String panelNumber;
    private String presentation;


    //    @Enumerated(EnumType.STRING)
//    private CStatus status;
//    @JsonIgnore
//    @ManyToMany(fetch = FetchType.EAGER)

    @JsonIgnore
    @ElementCollection(fetch=FetchType.EAGER)
    private Set<String> first = new TreeSet<>();

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> second = new TreeSet<>();

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> third = new TreeSet<>();

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> fourth = new TreeSet<>();

    @Column(name = "branch_id")
    private String branch;

    public Set<String> getFirstLevelServicesMeta() {
        return first;
    }

    public Set<String> getSecondLevelServicesMeta() {
        return second;
    }

    public Set<String> getThirdLevelServicesMeta() {
        return third;
    }

    public Set<String> getFourthLevelServicesMeta() {
        return fourth;
    }

    //    @JsonIgnoreProperties(allowGetters = true,allowSetters = false)
    @JsonIgnore
    public Set<String> getAllAssignedServices() {
        Set<String> services = new HashSet<>(0);
        services.addAll(first);
        if (second.size() > 0) {
            services.addAll(second);
        }
        if (third.size() > 0) {
            services.addAll(third);
        }
        if (fourth.size() > 0) {
            services.addAll(fourth);
        }
        return services;
    }

    public void setFirstLevelServicesMeta(Set<String> services) {

    }

    public void setSecondLevelServicesMeta(Set<String> services) {

    }

    public void setThirdLevelServicesMeta(Set<String> services) {

    }

    public void setFourthLevelServicesMeta(Set<String> services) {

    }

    public void setAllAssignedServices(Set<String> services) {

    }

    @JsonCreator
    public Counter(String name, String displayName, String description, String code, boolean active, Type type) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.code = code;
        this.active = active;
        this.type = type;
    }

    public Counter activate() {
        this.active = true;
        this.registerEvent(new CounterActivated(this));
        return this;
    }

    public Counter deactivate() {
        this.active = false;
        this.registerEvent(new CounterDeactivated(this));
        return this;
    }

    public Counter firstLevelService(String service) {
        this.first.add(service);
        this.registerEvent(ServiceAddedToCounter.of(this, service));
        return this;
    }

    public Counter secondLevelService(String service) {
        this.second.add(service);
        this.registerEvent(ServiceAddedToCounter.of(this, service));
        return this;
    }

    public Counter thirdLevelService(String service) {
        this.third.add(service);
        this.registerEvent(ServiceAddedToCounter.of(this, service));
        return this;
    }

    public Counter fourthLevelService(String service) {
        this.fourth.add(service);
        this.registerEvent(ServiceAddedToCounter.of(this, service));
        return this;
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class CounterActivated extends DomainEvent<Counter> {

        @AggregateReference
        final Counter counter;
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class CounterDeactivated extends DomainEvent<Counter> {

        @AggregateReference
        Counter counter;
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class ServiceAddedToCounter extends DomainEvent<Counter> {

        @AggregateReference
        Counter counter;

        @AggregateReference
        String service;
    }

    public enum Type {
        CLINIC,
        COUNTER,
        OFFICE,
        ROOM,
        MEETING_ROOM
    }

    public enum CStatus {
        OPEN,
        CLOSED,
        AUTOCALL,
        BREAK,
        FORCEAUTOCALL,
    }
}
