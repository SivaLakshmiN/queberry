package io.queberry.que.resources;

import io.queberry.que.config.Queue.QueueConfiguration;
import io.queberry.que.counter.CounterDTO;
import io.queberry.que.employee.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CounterAgentRequest {
    String counterId;
    String remarks;
    String serviceId;
    String subServiceId;
    String transferCounterId;
    String transferServiceId;
    String transferEmployeeId;
    String assistanceId;
    Integer number;
    String type = "";
    String employeeId;
    String subServiceData;
    EmployeeDTO employee;
    CounterDTO counter;
    QueueConfiguration.ServicePriority servicePriority;
    QueueConfiguration.QueueStrategy queueStrategy;
    String tokenId;
}
