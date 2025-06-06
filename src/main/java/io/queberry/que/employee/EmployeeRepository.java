package io.queberry.que.employee;

import io.queberry.que.branch.Branch;
import io.queberry.que.role.Role;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

//import io.queberry.que.Employee.Employee;
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
    Set<Employee> findByBranchesIn(Set<String> branches);

    Optional<Employee> findByLoggedCounter(String cid);

    @Query(value = "SELECT e FROM que_employee e WHERE e.id = :id")
    Optional<Employee> findEmployeeById(@Param("id") String id);

    @Query(value = "SELECT e FROM que_employee e WHERE e.username = :username")
    Optional<Employee> findEmployeeByUsername(@Param("username") String username);

//    Employee findByUsername(String username);

    Page<Employee> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

    int countAllByActiveTrue();

//    List<Employee> findByBranchIn(Set<String> branches);

    List<Employee> findAllByUsername(String username);

    Optional<Employee> findById(String id);

    List<Employee> findByActiveTrueAndBranchesAndRolesContaining(Branch branch, Role role);

}

