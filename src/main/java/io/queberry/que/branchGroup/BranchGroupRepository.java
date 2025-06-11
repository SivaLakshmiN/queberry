package io.queberry.que.branchGroup;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchGroupRepository extends JpaRepository<BranchGroup, String> {
    Optional<BranchGroup> findByName(String name);
}
