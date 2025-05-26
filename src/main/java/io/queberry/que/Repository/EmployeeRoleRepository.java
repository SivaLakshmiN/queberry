package io.queberry.que.Repository;

import io.queberry.que.Entity.EmployeeRole;
import io.queberry.que.Entity.EmployeeRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRoleRepository extends JpaRepository<EmployeeRole, EmployeeRoleId> {
}
