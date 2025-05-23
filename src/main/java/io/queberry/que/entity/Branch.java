package io.queberry.que.entity;

import io.queberry.que.controller.BranchController;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


@Setter
@Entity
@NoArgsConstructor
@Getter
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "BranchCache")
public class Branch extends AggregateRoot<Branch> {

    @Column(unique = true)
    private String name;

    private Locale locale;

    private ZoneId timeZoneId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    private LocalTime businessStart;

    private LocalTime businessEnd;

    @Column(name = "region")
    private String region;

    @Column(columnDefinition = "bit default 1")
    private boolean active;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean globalServices;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "branch_services", joinColumns = @JoinColumn(name = "branch_id"))
    @Column(name = "service")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "ServiceCache")
    private Set<String> services = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "branch_service_groups", joinColumns = @JoinColumn(name = "branch_id"))
    @Column(name = "service_group")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "ServiceGroupCache")
    private Set<String> serviceGroup = new HashSet<>();

    private String branchKey;

    @Column(name = "is_global_appointment", nullable = false, columnDefinition = "bit default 1")
    private boolean isGlobalAppointment;

    private String comPort;

    @Column(columnDefinition = "int default 0")
    private int capacity = 0;

}

