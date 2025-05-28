package io.queberry.que.DTO;
import io.queberry.que.Entity.BreakConfiguration;
import io.queberry.que.Entity.BreakSession;
import io.queberry.que.Entity.Employee;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@Slf4j
public class BreakSessionDTO {
    private String id;
    private SessionsEmployeeDTO employeeDTO;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BreakConfiguration breakConfiguration;

    public BreakSessionDTO(BreakSession breakSession){
        this.id=breakSession.getId();
        this.employeeDTO = getEmployeeDTO(breakSession.getEmployee());
        this.startTime = breakSession.getStartTime();
        this.endTime = breakSession.getEndTime();
        this.breakConfiguration = breakSession.getBreakConfiguration();
    }

    private SessionsEmployeeDTO getEmployeeDTO(Employee employee){
        return new SessionsEmployeeDTO(employee);
    }
}