package io.queberry.que.Employee;

import io.queberry.que.Branch.BranchesDTO;
import io.queberry.que.Region.RegionDTO;
import io.queberry.que.Role.RoleDTO;
import io.queberry.que.dto.ServicesDTO;
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
    private List<BranchesDTO> branches;
    private List<ServicesDTO> services;
    private Set<String> second;
    private Set<String> third;
    private Set<String> fourth;


    public EmployeeRequest(Employee employee,
                           RegionDTO region,
                           List<BranchesDTO> branches,
                           List<ServicesDTO> services) {
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
        this.second = employee.getSecond();
        this.third = employee.getThird();
        this.fourth = employee.getFourth();
    }

    public EmployeeRequest() {

    }
}