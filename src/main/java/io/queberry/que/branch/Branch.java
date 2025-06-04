package io.queberry.que.branch;

import io.queberry.que.entity.Address;
import io.queberry.que.entity.AggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Id
    private String id;

    @Column(unique = true)
    private String name;

    private Locale locale;

    private ZoneId timeZoneId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    private LocalTime businessStart;

    private LocalTime businessEnd;

    @Column(name = "region_id")
    private String region;

    @Column(columnDefinition = "bit default 1")
    private boolean active;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean globalServices;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "branch_services", joinColumns = @JoinColumn(name = "branch_id"))
    @Column(name = "services_id")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "ServiceCache")
    private Set<String> services = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "branch_service_groups", joinColumns = @JoinColumn(name = "branch_id"))
    @Column(name = "service_group_id")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "ServiceGroupCache")
    private Set<String> serviceGroup = new HashSet<>();

    private String branchKey;

    @Column(name = "is_global_appointment", nullable = false, columnDefinition = "bit default 1")
    private boolean isGlobalAppointment;

    private String comPort;

    @Column(columnDefinition = "int default 0")
    private int capacity = 0;

}

