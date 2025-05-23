package io.queberry.que.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "RegionCache")
public class Region extends AggregateRoot<Region> {

    @Column(unique = true)
    private String name;
    private boolean enableBranchGroup;

    @Column(columnDefinition = "bit default 1")
    private boolean active;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> branchGroup = new HashSet<>(0);
}
