package io.queberry.que.branchGroup;

import io.queberry.que.entity.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BranchGroup extends AggregateRoot<BranchGroup> {

    @Column(unique = true)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> branches = new HashSet<>(0);

    private boolean active = true;
}
