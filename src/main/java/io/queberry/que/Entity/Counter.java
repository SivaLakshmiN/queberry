package io.queberry.que.Entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.w3c.dom.events.EventTarget;

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

    //@NotNull(message = "Counter name is mandatory!!")
    @Column(columnDefinition = "nvarchar(255)")
    private String name;

    @Column(columnDefinition = "nvarchar(255)",nullable = false)
    //@NotNull(message = "Display Name is mandatory.")
    private String displayName;

   //@NotNull(message = "Counter description is mandatory!!")
    @Column(columnDefinition = "nvarchar(255)")
    private String description;

    @Column(columnDefinition = "nvarchar(255)")
    //@NotNull(message = "Counter code is mandatory!!")
    private String code;

    @Column(columnDefinition = "bit default 1")
    private boolean active = true;

    @Enumerated(EnumType.STRING)
    //@NotNull(message = "Counter type to be either CLINIC,COUNTER,OFFICE,ROOM or MEETING_ROOM!!")
    private Type type;

    @Column(columnDefinition = "bit default 0")
    private boolean inUse = false;

    private String colorCode;
    private String panelNumber;
    private String presentation;


    //    @Enumerated(EnumType.STRING)
//    private CStatus status;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "ServiceCache")
    private Set<Service> first = new TreeSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Service> second = new TreeSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Service> third = new TreeSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Service> fourth = new TreeSet<>();

    @ManyToOne
    @JoinColumn(name = "branch_id")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "BranchCache")
    private Branch branch;

    public Set<Service> getFirstLevelServicesMeta() {
        return first;
    }

    public Set<Service> getSecondLevelServicesMeta() {
        return second;
    }

    public Set<Service> getThirdLevelServicesMeta() {
        return third;
    }

    public Set<Service> getFourthLevelServicesMeta() {
        return fourth;
    }

    //    @JsonIgnoreProperties(allowGetters = true,allowSetters = false)
    @JsonIgnore
    public Set<Service> getAllAssignedServices() {
        Set<Service> services = new HashSet<>(0);
        services.addAll(first);
        if(second.size() > 0){
            services.addAll(second);
        }
        if(third.size() > 0){
            services.addAll(third);
        }
        if(fourth.size() >0){
            services.addAll(fourth);
        }
        return services;
    }

    public void setFirstLevelServicesMeta(Set<Service> services) {

    }

    public void setSecondLevelServicesMeta(Set<Service> services) {

    }

    public void setThirdLevelServicesMeta(Set<Service> services) {

    }

    public void setFourthLevelServicesMeta(Set<Service> services) {

    }

    public void setAllAssignedServices(Set<Service> services) {

    }

    @JsonCreator
    public Counter(String name, String displayName,String description, String code, boolean active, Type type) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.code = code;
        this.active = active;
        this.type = type;
    }

    public Counter activate(){
        this.active = true;
        this.registerEvent(new CounterActivated(this));
        return this;
    }

    public Counter deactivate(){
        this.active = false;
        this.registerEvent(new CounterDeactivated(this));
        return this;
    }

    public Counter firstLevelService(Service service){
        this.first.add(service);
        this.registerEvent(ServiceAddedToCounter.of(this,service));
        return this;
    }

    public Counter secondLevelService(Service service){
        this.second.add(service);
        this.registerEvent(ServiceAddedToCounter.of(this,service));
        return this;
    }

    public Counter thirdLevelService(Service service){
        this.third.add(service);
        this.registerEvent(ServiceAddedToCounter.of(this,service));
        return this;
    }

    public Counter fourthLevelService(Service service){
        this.fourth.add(service);
        this.registerEvent(ServiceAddedToCounter.of(this,service));
        return this;
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class CounterActivated extends DomainEvent<Counter> {

        @AggregateReference
        final Counter counter;

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

    @Value
    @EqualsAndHashCode(callSuper = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class CounterDeactivated extends DomainEvent<Counter> {

        @AggregateReference
        final Counter counter;

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

    @Value
    @EqualsAndHashCode(callSuper = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class ServiceAddedToCounter extends DomainEvent<Counter> {

        @AggregateReference
        final Counter counter;

        @AggregateReference
        final Service service;

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

    public enum Type{
        CLINIC,
        COUNTER,
        OFFICE,
        ROOM,
        MEETING_ROOM
    }

    public enum CStatus{
        OPEN,
        CLOSED,
        AUTOCALL,
        BREAK,
        FORCEAUTOCALL,
    }
}

