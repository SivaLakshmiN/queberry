package io.queberry.que.employee;

import io.queberry.que.service.ServiceDTO;
import io.queberry.que.branch.BranchDTO;
import io.queberry.que.region.RegionDTO;
import io.queberry.que.role.RoleDTO;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class EmployeeRequest {
    private String id;
    private String firstname;
    private String lastname;
    private String username;
    private boolean active;

    private RegionDTO region;
    private List<RoleDTO> roles;
    private List<BranchDTO> branches;
    private List<ServiceDTO> services;
    private Set<ServiceDTO> second;
    private Set<ServiceDTO> third;
    private Set<ServiceDTO> fourth;

    public EmployeeRequest(Employee employee,
                           RegionDTO region,
                           List<BranchDTO> branches,
                           List<ServiceDTO> services,
                           Set<ServiceDTO> second,
                           Set<ServiceDTO> third,
                           Set<ServiceDTO> fourth) {
        this.id = employee.getId();
        this.firstname = employee.getFirstname();
        this.lastname = employee.getLastname();
        this.username = employee.getUsername();
        this.active = employee.isActive();
        this.region = region;
        this.branches = branches;
        this.roles = employee.getRoles().stream()
                .map(role -> new RoleDTO(role.getId()))
                .collect(Collectors.toList());
        this.services = services;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public EmployeeRequest() {

    }
}