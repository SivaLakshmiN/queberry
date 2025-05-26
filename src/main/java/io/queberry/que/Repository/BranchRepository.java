package io.queberry.que.Repository;

import io.queberry.que.Entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, String> {
    Branch findByBranchKey(String branchKey);
}
