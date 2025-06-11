package io.queberry.que.role;

import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class ReportingBean {
    private Report live;
    private ServiceReport serviceReport;
    private CounterReport counterReport;
    private EmployeeReport employeeReport;
    private SubServiceReport subServiceReport;
}
