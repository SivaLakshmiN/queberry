package io.queberry.que.repository;

import io.queberry.que.entity.Branch;
import io.queberry.que.entity.Employee;
import io.queberry.que.entity.Role;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

//import io.queberry.que.entity.Employee;
import io.queberry.que.entity.Employee;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    @QueryHints(value = {
            @QueryHint(name = "org.hibernate.cacheable", value = "true"),
            @QueryHint(name = "org.hibernate.cacheRegion", value = "EmployeeUserCache")
    })
    @Query("SELECT e FROM que_employee e WHERE e.username = :username")
    Employee findByUsername(@Param("username") String username);

//    Set<Employee> findByBranch(Set<String> branches);

//    Set<Employee> findByBranchesIn(Set<String> branches);
=======
    Set<Employee> findByBranchIn(Set<String> branches);

    Optional<Employee> findByLoggedCounter(String cid);

    @Query(value = "SELECT e FROM que_employee e WHERE e.id = :id")
    Optional<Employee> findEmployeeById(@Param("id") String id);

    @Query(value = "SELECT e FROM que_employee e WHERE e.username = :username")
    Optional<Employee> findEmployeeByUsername(@Param("username") String username);

//    Employee findByUsername(String username);

    Page<Employee> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

    int countAllByActiveTrue();

    List<Employee> findByBranchesIn(Set<String> branches);

    List<Employee> findAllByUsername(String username);

    Optional<Employee> findById(String id);

    List<Employee> findByActiveTrueAndBranchesAndRolesContaining(Branch branch, Role role);

}

