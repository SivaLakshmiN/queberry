package io.queberry.que.role;

import io.queberry.que.employee.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
public class EmployeeReport {
    private Set<EmployeeLineItem> reports = new HashSet<>(0);

    public Collection<EmployeeLineItem> getReports() {
        return reports.stream()
                .sorted(Comparator.comparing(EmployeeLineItem::employeeName))
                .collect(Collectors.toList());
    }

    public void add(EmployeeLineItem employeeLineItem){reports.add(employeeLineItem);}



    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmployeeLineItem{
        private Employee employee;
        private Report report;
        public String employeeName(){return employee.getUsername();}
    }
}
