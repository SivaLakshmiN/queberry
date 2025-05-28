package io.queberry.que.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
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

    @ManyToOne
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "RegionCache")
    private Region region;

    @Column(columnDefinition = "bit default 1")
    private boolean active;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean globalServices;

    @ManyToMany(fetch = FetchType.EAGER)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "ServiceCache")
    private Set<Service> services = new HashSet<>(0);

//    @ManyToMany(fetch = FetchType.EAGER)
//    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "ServiceGroupCache")
//    private Set<ServiceGroup> serviceGroup = new HashSet<>(0);

    private String branchKey;

    @Column(name = "is_global_appointment", nullable = false, columnDefinition = "bit default 1")
    private boolean isGlobalAppointment;

    private String comPort;

    @Column(columnDefinition = "int default 0")
    private int capacity = 0;

}