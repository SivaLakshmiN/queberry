package io.queberry.que.entity;

import java.io.Serializable;
import java.util.Objects;

public class EmployeeRoleId implements Serializable {

    private String employeeId;
    private String roleId;

    public EmployeeRoleId() {}

    public EmployeeRoleId(String employeeId, String roleId) {
        this.employeeId = employeeId;
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeRoleId)) return false;
        EmployeeRoleId that = (EmployeeRoleId) o;
        return Objects.equals(employeeId, that.employeeId) &&
                Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, roleId);
    }
}
