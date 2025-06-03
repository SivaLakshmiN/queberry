package io.queberry.que.Region;

import io.queberry.que.entity.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
