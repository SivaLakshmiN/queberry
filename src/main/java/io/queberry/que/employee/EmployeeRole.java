package io.queberry.que.employee;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "que_employee_roles")
@IdClass(EmployeeRoleId.class)
@Getter
@Setter
public class EmployeeRole {

    @Id
    @Column(name = "que_employee_id")
    private String employeeId;

    @Id
    @Column(name = "roles_id")
    private String roleId;

    public EmployeeRole() {
    }

    public EmployeeRole(String employeeId, String roleId) {
        this.employeeId = employeeId;
        this.roleId = roleId;
    }

}
