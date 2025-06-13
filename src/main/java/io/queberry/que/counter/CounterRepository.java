package io.queberry.que.counter;

import io.queberry.que.branch.Branch;
import io.queberry.que.service.Service;
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
    Set<Counter> findAllByIdNotInAndBranchAndActiveIsTrueAndInUseFalse(Set<String> ids, Branch b, Sort sort);
    Set<Counter> findAllByIdNotInAndBranchAndActiveIsTrueAndInUseFalse(Set<String> ids, String b, Sort sort);
    Set<Counter> findAllByActiveIsTrueAndBranchAndInUseFalse(Branch b,Sort sort);
    Set<Counter> findAllByActiveIsTrueAndBranchAndInUseFalse(String b,Sort sort);
    @Query(value = "SELECT * FROM que_counter WHERE id = :id", nativeQuery = true)
    Optional<Counter> findCounterById(@Param("id") String id);
    List<Counter> findByBranchAndInUse(String b, boolean t);
    Set<Counter> findByBranchAndInUseAndFirstIsInOrSecondIsInOrThirdIsInOrFourthIsIn(Branch b, boolean t, Set<Service> s1, Set<Service> s2, Set<Service> s3, Set<Service> s4);
    List<Counter> findByIdInAndBranch(Set<String> ids, String b);
//    Set<Counter> findAllByBranch(Set<String> b);
}

