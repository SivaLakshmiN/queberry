package io.queberry.que.dto;

import io.queberry.que.entity.Address;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.Set;

@Setter
@Getter
@ToString
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@Slf4j
public class BranchDTO {

    private String id;

    private String name;

//    private Locale locale;
//
//    private ZoneId timeZoneId;

    private Address address;

    private LocalTime businessStart;

    private LocalTime businessEnd;

    private boolean active;

    private boolean globalServices;

    private String branchKey;

    private String region;

    private boolean isGlobalAppointment;
    private String comPort;
    private int capacity;
    private Set<String> services;
    private Set<String> serviceGroups;

//    public BranchDTO(String id,
//                     String name,
//                     Address address,
//                     LocalTime businessStart,
//                     LocalTime businessEnd,
//                     boolean active,
//                     boolean globalServices,
//                     String branchKey,
//                     String region,
//                     boolean isGlobalAppointment,
//                     String comPort,
//                     int capacity,
//                     Set<String> services,
//                     Set<String> serviceGroups) {
//        this.id = id;
//        this.name = name;
//        this.address = address;
//        this.businessStart = businessStart;
//        this.businessEnd = businessEnd;
//        this.active = active;
//        this.globalServices = globalServices;
//        this.branchKey = branchKey;
//        this.region = region;
//        this.isGlobalAppointment = isGlobalAppointment;
//        this.comPort = comPort;
//        this.capacity = capacity;
//        this.services = services;
//        this.serviceGroups = serviceGroups;
//    }

}
