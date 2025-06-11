package io.queberry.que.role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NonAggregatedReportService {

    private final ReportingService reportingService;

    public List<Report> getHomeReport(LocalDate from, LocalDate to){
        Set<Report> reports = new HashSet<>(0);
        LocalDate reportDate = from;
        while (reportDate.compareTo(to) <= 0) {
            reports.add(reportingService.getHomeReport(reportDate,reportDate));
            reportDate = reportDate.plusDays(1);
        }

        return reports.stream()
                .sorted(Comparator.comparing(Report::getFrom))
                .collect(Collectors.toList());
    }

    public List<Report> getHomeReportByBranch(LocalDate from, LocalDate to, String branchKey){
        Set<Report> reports = new HashSet<>(0);
        LocalDate reportDate = from;
        while (reportDate.compareTo(to) <= 0) {
            reports.add(reportingService.getHomeReportByBranch(reportDate,reportDate,branchKey));
            reportDate = reportDate.plusDays(1);
        }

        return reports.stream()
                .sorted(Comparator.comparing(Report::getFrom))
                .collect(Collectors.toList());
    }

    public Set<CounterReport> getAllCountersReportByBranch(LocalDate from, LocalDate to, ReportRequest.Parties parties, String branchId){
        Set<CounterReport> reports = new HashSet<>(0);
        LocalDate reportDate = from;
        while (reportDate.compareTo(to) <= 0) {
            reports.add(reportingService.getAllCountersReportByBranch(reportDate,reportDate,parties,branchId));
            reportDate = reportDate.plusDays(1);
        }
        return reports;
    }

    public Set<ServiceReport> getAllServicesReportByBranch(LocalDate from, LocalDate to, ReportRequest.Parties parties, String branchKey, String region){
        Set<ServiceReport> reports = new HashSet<>(0);
        LocalDate reportDate = from;
        while (reportDate.compareTo(to) <= 0) {
            reports.add(reportingService.getAllServicesReportByBranch(reportDate,reportDate,parties,branchKey,region));
            reportDate = reportDate.plusDays(1);
        }
        return reports;
    }

//    public Set<EmployeeReport> getAllEmployeesReport(LocalDate from, LocalDate to, ReportRequest.Parties parties){
//        Set<EmployeeReport> reports = new HashSet<>(0);
//        LocalDate reportDate = from;
//        while (reportDate.compareTo(to) <= 0) {
//            reports.add(reportingService.getAllEmployeesReport(reportDate,reportDate,parties));
//            reportDate = reportDate.plusDays(1);
//        }
//        return reports;
//    }

    public Set<EmployeeReport> getAllEmployeesReportByBranch(LocalDate from, LocalDate to, ReportRequest.Parties parties, String branchKey){
        Set<EmployeeReport> reports = new HashSet<>(0);
        LocalDate reportDate = from;
        while (reportDate.compareTo(to) <= 0) {
            reports.add(reportingService.getAllEmployeesReportByBranch(reportDate,reportDate,parties, branchKey));
            reportDate = reportDate.plusDays(1);
        }
        return reports;
    }

    public Set<SubServiceReport> getAllSubServicesReportByBranch(LocalDate from, LocalDate to, ReportRequest.Parties parties, String branchKey){
        Set<SubServiceReport> reports = new HashSet<>(0);
        LocalDate reportDate = from;
        while (reportDate.compareTo(to) <= 0) {
            reports.add(reportingService.getAllSubServicesReportByBranch(reportDate,reportDate,parties,branchKey));
            reportDate = reportDate.plusDays(1);
        }
        return reports;
    }
}
