package io.queberry.que.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class EmpDashboardDtls {
    public String InQueWaitTime;
    public String InQueAvgWaitTime;
    private String employeeId;
    private String employeeName;
    private int totalTokens;
    private int totalParked;
    private int totalNoShow;
    private int totalCompleted;
    private int totalTransferred;
    private String avgWaitTime;
    private String avgServeTime;
    private String inQueWaitTime;
    private String inQueAvgWaitTime;
}

