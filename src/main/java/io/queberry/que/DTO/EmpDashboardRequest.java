package io.queberry.que.DTO;

import lombok.Data;

import java.util.List;

@Data
public class EmpDashboardRequest {
    private String employeeId;
    private String startDate;    // format: "dd-MM-yyyy"
    private String endDate;      // format: "dd-MM-yyyy"
    private List<ServiceDTO> services;
}
