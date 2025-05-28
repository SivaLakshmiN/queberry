package io.queberry.que.Repository;

import io.queberry.que.Entity.Branch;
import io.queberry.que.Entity.Employee;
import io.queberry.que.Entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    Employee findByUsername(String username);

    Page<Employee> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

    int countAllByActiveTrue();

    List<Employee> findByBranchesIn(Set<String> branches);

    List<Employee> findAllByUsername(String username);

    Optional<Employee> findById(String id);

    List<Employee> findByActiveTrueAndBranchesAndRolesContaining(Branch branch, Role role);


}