package io.queberry.que.repository;

import io.queberry.que.entity.EmployeeRole;
import io.queberry.que.entity.EmployeeRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRoleRepository extends JpaRepository<EmployeeRole, EmployeeRoleId> {
}
