package io.queberry.que.Counter;

import io.queberry.que.Branch.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CounterRepository extends JpaRepository<Counter,String>{
    Counter findByName(String name);
    Page<Counter> findByBranchAndCodeContainingIgnoreCase(Branch b, String str, Pageable p);
    Set<Counter> findByActiveTrue(Sort sort);
    Set<Counter> findByBranchAndActiveTrue(Branch b, Sort sort);
    List<Counter> findByIdIn(Set<String> ids);
    List<Counter> findByIdInAndBranch(Set<String> ids, Branch b);
    List<Counter> findAllByBranch(Branch b);
    Page<Counter> findAllByBranch(Branch b, Pageable pageable);
    Set<Counter> findAllByIdNotInAndBranchAndActiveIsTrueAndInUseFalse(Set<String> ids, Branch b, Sort sort);
    Set<Counter> findAllByIdNotInAndBranchAndActiveIsTrueAndInUseFalse(Set<String> ids, String b, Sort sort);
    Set<Counter> findAllByActiveIsTrueAndBranchAndInUseFalse(Branch b,Sort sort);
    Set<Counter> findAllByActiveIsTrueAndBranchAndInUseFalse(String b,Sort sort);
    @Query(value = "SELECT * FROM que_counter WHERE id = :id", nativeQuery = true)
    Optional<Counter> findCounterById(@Param("id") String id);
    List<Counter> findByBranchAndInUse(String b, boolean t);
}

