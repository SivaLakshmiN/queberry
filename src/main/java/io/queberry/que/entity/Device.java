package io.queberry.que.entity;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.queberry.que.Counter.Counter;
import io.queberry.que.ServiceGroup.ServiceGroup;
import io.queberry.que.anotation.AggregateReference;
import io.queberry.que.exception.QueueException;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
@Slf4j
@Entity
@Getter
@Setter
@ToString(callSuper = true)
@Table(indexes = {@Index(name="device_br_index",columnList = "pairedBy")})
public class Device extends AggregateRoot<Device>{

    private String name = "";

    @Column(unique = true)
    private String deviceId;

    private Type type;

    private Status status;

//    @ManyToOne
//    private Playlist playlist;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Counter> counters = new TreeSet<>();

//    @ManyToOne
//    private Template template;

//    @ManyToMany(fetch = FetchType.EAGER)
//    private Set<Ticker> tickers = new HashSet<>(0);

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<ServiceGroup> serviceGroups = new HashSet<>(0);

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Service> services = new HashSet<>(0);

    private String pairedBy;

//    @OneToOne(cascade = CascadeType.ALL)
//    private DeviceTheme theme;
//
//    @ManyToOne
//    private Survey survey;

    private LocalDateTime createdAt = LocalDateTime.now();

//    private String branchKey;

    public Device(String deviceId, Type type){
        Assert.notNull(deviceId, "Device ID cannot be null");
        Assert.notNull(type, "Device type cannot be null");

        this.deviceId = deviceId;
        this.type = type;
        this.status = Status.PAIRING_REQUEST;

        this.andEvent(DevicePairingRequested.of(this));

    }

    public Device pair(String pairedBy){
        this.status = Status.REGISTERED;
//        this.pairedBy = pairedBy;
        return this.andEvent(DeviceUpdated.of(this));
    }

    public Device rejectPairingRequest(){
        this.status = Status.REJECTED;
        return this.andEvent(DeviceUpdated.of(this));
    }

    public Device(){
        this.deviceId = null;
        this.type = null;
        this.name = "";
    }

    public Device name(String name){
        validate();
        this.name = name;
        return this;
    }

//    public Device theme(DeviceTheme theme){
//        validate();
//        if (this.theme != null)
//            this.theme.change(theme);
//        else
//            this.theme = theme;
//        return this;
//    }
//
//    public Device apply(Template template){
//        validate();
//        if (this.type.equals(template.getDeviceType())){
//            this.template = template;
//            return andEvent(DeviceUpdated.of(this));
//
//        }
//        throw new QueueException("Cannot apply template to device as the types are not matching", HttpStatus.PRECONDITION_FAILED);
//    }
//
//
//    public Device tickers(Set<Ticker> tickers){
//        validate();
//        this.tickers.clear();
//        this.tickers.addAll(tickers);
//        return andEvent(DeviceUpdated.of(this));
//    }

    public Device serviceGroups(Set<ServiceGroup> serviceGroups){
        validate();
        this.serviceGroups.clear();
        this.serviceGroups.addAll(serviceGroups);
        return this;
    }

    public Device services(Set<Service> services){
        validate();
        this.services.clear();
        this.services.addAll(services);
        return this;
    }

//    public Device apply(Playlist playlist){
//        validate();
//        if (this.type==Type.SIGNAGE){
//            this.playlist = playlist;
//            return andEvent(DeviceUpdated.of(this));
//        }
//        throw new QueueException("Cannot apply a playlist to "+type,HttpStatus.PRECONDITION_FAILED);
//    }
//
//    public Device apply(Survey survey){
//        validate();
//        if (this.type==Type.SURVEY){
//            this.survey = survey;
//            return andEvent(DeviceUpdated.of(this));
//        }
//        throw new QueueException("Cannot apply a survey to "+type,HttpStatus.PRECONDITION_FAILED);
//    }

    public Device assign(Set<Counter> counters){
        validate();
        if (!(this.type == Type.COUNTER_DISPLAY || this.type == Type.SIGNAGE || this.type == Type.SURVEY))
            throw new QueueException("Counter can be only assigned to Counter Display.This device is a "+type,HttpStatus.PRECONDITION_FAILED);
        if (this.type == Type.SURVEY)
            if (counters.size() > 1)
                throw new QueueException("Only one counter can be assigned to a "+type,HttpStatus.PRECONDITION_FAILED);
        this.counters.addAll(counters);
        return andEvent(DeviceUpdated.of(this));
    }

    public Device unAssignCounter(){
        validate();
        if (!(this.type == Type.COUNTER_DISPLAY || this.type == Type.SIGNAGE))
            throw new QueueException("Counter can be only unassigned from a Counter Display.This device is a "+type,HttpStatus.PRECONDITION_FAILED);
        this.counters.clear();
        return andEvent(DeviceUpdated.of(this));
    }

    public void unAssignServices(){
        validate();
        this.services.clear();
        andEvent(DeviceUpdated.of(this));
    }

    public Device unassignServiceGroup()
    {
        validate();
        this.serviceGroups.clear();
        return andEvent(DeviceUpdated.of(this));
    }

//    public void unassignTickers()
//    {
//        validate();
//        this.tickers.clear();
//        andEvent(DeviceUpdated.of(this));
//    }
//
//    public Device unassignPlaylist(){
//        validate();
//        this.playlist = null;
//        return andEvent(DeviceUpdated.of(this));
//    }
//
//    public Device unassignTemplate(){
//        validate();
//        this.template = null;
//        return andEvent(DeviceUpdated.of(this));
//    }
//
//    public Playlist getPlaylistMeta(){
//        //validate();
//        return this.getPlaylist();
//    }
//
//    public Template getTemplateMeta(){
//        //validate();
//        return this.getTemplate();
//    }
//
//    public Set<Ticker> getTickersMeta(){
//        //validate();
//        return this.tickers;
//    }

    public Set<Counter> getCountersMeta(){
        return this.counters;
    }

    public Set<ServiceGroup> getServiceGroupsMeta(){
        return this.serviceGroups;
    }

    public Set<Service> getServicesMeta(){
        return this.services;
    }


    public void validate(){
        if (status == Status.PAIRING_REQUEST)
            throw new QueueException("Device is not yet paired. Please Pair the device first",HttpStatus.PRECONDITION_FAILED);
    }

    // Register a new Device.
    @JsonCreator
    public static Device register(String deviceId, Type type){
        return new Device(deviceId, type);
    }

    /*@Value
    @EqualsAndHashCode(callSuper = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class DeviceAssignedToCounter extends DomainEvent<Device>{

        @AggregateReference
        Device device;

        @AggregateReference
        Counter counter;
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class DeviceCounterUnassigned extends DomainEvent<Device>{

        @AggregateReference
        Device device;
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class DevicePairingRequested extends DomainEvent<Device>{

        @AggregateReference
        Device device;
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class DeviceRegistered extends DomainEvent<Device>{

        @AggregateReference
        Device device;
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class DeviceRejected extends DomainEvent<Device>{

        @AggregateReference
        Device device;
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class TemplateApplied extends DomainEvent<Device>{

        @AggregateReference
        Device device;

        @AggregateReference
        Template template;
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class TemplateUnAssigned extends DomainEvent<Device>{

        @AggregateReference
        Device device;
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class PlaylistApplied extends DomainEvent<Device>{

        @AggregateReference
        Device device;

        @AggregateReference
        Playlist playlist;
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class PlaylistUnassigned extends DomainEvent<Device>{

        @AggregateReference
        Device device;
    }*/

    @Value
    @EqualsAndHashCode(callSuper = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class DevicePairingRequested extends DomainEvent<Device>{

        @AggregateReference
        Device device;
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class DeviceUpdated extends DomainEvent<Device>{

        @AggregateReference
        Device device;
    }

    public enum Status{
        PAIRING_REQUEST,
        REGISTERED, // Device has registered itself with a unique ID, Platform, OS version and preferable available functions like printing
        REJECTED,
        ALIVE // Device is pinging periodically
    }

    public enum Type{
        DISPENSER,
        SIGNAGE,
        COUNTER_DISPLAY,
        HUB,
        SURVEY
    }
}
