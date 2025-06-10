package io.queberry.que.employee;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Setter
@Getter
@ToString
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@Slf4j
public class EmployeeSessionsDTO {
    private String id;
    private SessionsEmployeeDTO employeeDTO;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;

    public EmployeeSessionsDTO(EmployeeSessions employeeSessions){
        this.id=employeeSessions.getId();
        this.employeeDTO = getEmployeeDTO(employeeSessions.getEmployee());
        this.loginTime = employeeSessions.getLoginTime();
        this.logoutTime = employeeSessions.getLogoutTime();
    }

    private SessionsEmployeeDTO getEmployeeDTO(Employee employee){
        return new SessionsEmployeeDTO(employee);
    }

}
