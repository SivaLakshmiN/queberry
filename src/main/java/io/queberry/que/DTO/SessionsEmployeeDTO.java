package io.queberry.que.DTO;

import io.queberry.que.Entity.Employee;
import io.queberry.que.Entity.Role;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@ToString
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@Slf4j
public class SessionsEmployeeDTO {
    private String id;
    private String username;
    private Set<Role> roles;
    private String tenant;
    private String loggedCounter;
    private LocalDateTime loggedTime;

    public SessionsEmployeeDTO(Employee employee){
        this.id=employee.getId();
        this.username = employee.getUsername();
        this.tenant = employee.getTenant();
        this.roles = employee.getRoles();
        this.loggedCounter = employee.getLoggedCounter();
        this.loggedTime = employee.getLoggedTime();
    }
}
