package io.queberry.que.repository;

import io.queberry.que.entity.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface BranchRepository extends JpaRepository<Branch,String> {

    Page<Branch> findByRegion(String region, Pageable pageable);

    Set<Branch> findByGlobalServicesTrueAndActiveTrueAndRegion(String region);
    Branch findByBranchKey(String branchKey);
    Set<Branch> findByActiveTrue();
    String countAllByActiveTrue();
}

