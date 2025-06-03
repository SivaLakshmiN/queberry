package io.queberry.que.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.queberry.que.SubService.SubService;
import io.queberry.que.anotation.AggregateReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity(name = "que_service")
@Table(name = "que_service",  uniqueConstraints = {
        @UniqueConstraint(columnNames = {"code", "region_id"})
})
@Getter
@Setter
@ToString
@NoArgsConstructor
//@EqualsAndHashCode(of = "name")
//@AllArgsConstructor
//@RequiredArgsConstructor(staticName = "of")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "ServiceCache")
public class Service extends AggregateRoot<Service> implements Comparable<Service>{

    @Id
    private String id;

    @Column(unique = true,columnDefinition = "nvarchar(255)",nullable = false)
    private String name;

    @Column(columnDefinition = "nvarchar(255)",nullable = false)
    private String displayName;

    @Column(columnDefinition = "nvarchar(255)")
    @ElementCollection(fetch = FetchType.EAGER)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "ServiceNamesCache")
    private Map<String,String> names = new HashMap<>(0);

    @Column(columnDefinition = "nvarchar(255)", nullable = false)
    private String description;

    @Column(columnDefinition = "nvarchar(255)")
    private String longDescription;

    @Column(columnDefinition = "nvarchar(255)", nullable = false)
    private String code;

    private boolean active = true;

    private Integer sequenceStart;
    private Integer sequenceEnd;
    @Column(length = 10)
    private String seperator;
    private Integer numberOfTickets;
    private String message;
    private Integer serveSla;
    private Integer waitSla;
    @Column(length = 10)
    private String serviceColor;

    @Column(columnDefinition = "bit default 0")
    private boolean sharedSeq;

    @Column(name = "sharedSequence_id")
    private String sharedSequence;

    @Column(name = "virtual_service", columnDefinition = "bit default 0")
    private Boolean virtualService = Boolean.FALSE;
    @Column(name = "prior_booking_days", columnDefinition = "int default 10")
    private int priorBookingDays = 10;
    @Column(name = "prior_cancel_hrs", columnDefinition = "int default 4")
    private int priorCancelHrs = 4;
    @Column(name = "appt_slot_duration", columnDefinition = "int default 10")
    private int apptSlotDuration = 10;

//    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
//    private Set<String> subServices = new HashSet<>(0);

    @Column(name = "subServices_id")
    private String subServices;

    @Column(name = "subServiceGroup_id")
    private String subServiceGroup;



    @Column(columnDefinition = "bit default 0")
    private boolean format;

    @Column(name = "region_id")
    private String region;

    public Service activate(){
        this.active = true;
        this.registerEvent(new ServiceActivated(this));
        return this;
    }

    public Service deactivate(){
        this.active = false;
        this.registerEvent(new ServiceDeactivated(this));
        return this;
    }

//    public Service subservices(Set<SubService> subService){
////        this.subServices.clear();
////        this.subServices.addAll(services);
//        this.subServices.addAll(subService);
//        return this;
//    }

//    public Service addSubservices(SubService services){
    ////        this.subServices.clear();
    ////        this.subServices.addAll(services);
//        return this;
//    }


    @JsonCreator
    public Service(String name, String description, String code, boolean active, Integer sequenceStart, Integer sequenceEnd, String seperator, Integer numberOfTickets, Set<SubService> subServices, String message, Map<String,String> names) {
        this.name = name;
        this.description = description;
        this.code = code;
        this.active = active;
        this.sequenceStart = sequenceStart;
        this.sequenceEnd = sequenceEnd;
        this.seperator = seperator;
        this.numberOfTickets = numberOfTickets == null ? 0 : numberOfTickets;
        this.message = message;
        this.names = names;
//        if (subServices != null)
//            this.subServices.addAll(subServices);
    }

    @Override
    public int compareTo(Service o) {
        return this.getId().compareTo(o.getId());
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class ServiceActivated extends DomainEvent<Service> {

        @AggregateReference
        Service service;
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class ServiceDeactivated extends DomainEvent<Service> {

        @AggregateReference
        Service service;
    }

//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    public Set<SubService> getSubServiceMeta(){
//        return subServices;
//    }


}
