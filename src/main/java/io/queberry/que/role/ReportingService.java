package io.queberry.que.role;

import io.queberry.que.assistance.Assistance;
import io.queberry.que.assistance.AssistanceRepository;
import io.queberry.que.branch.Branch;
import io.queberry.que.branch.BranchRepository;
import io.queberry.que.config.Break.BreakSessionRepository;
import io.queberry.que.config.Queue.QueueConfigurationRepository;
import io.queberry.que.counter.Counter;
import io.queberry.que.counter.CounterRepository;
import io.queberry.que.employee.*;
import io.queberry.que.enums.Type;
import io.queberry.que.home.Home;
import io.queberry.que.home.HomeRepository;
import io.queberry.que.region.Region;
import io.queberry.que.service.ServiceRepository;
import io.queberry.que.session.SessionRepository;
import io.queberry.que.subTransaction.SubTransaction;
import io.queberry.que.subTransaction.SubTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportingService {

    private final EmployeeSessionRepository employeeSessionRepository;
    private final HomeRepository homeRepository;
    private final BranchRepository branchRepository;

    private final AssistanceRepository assistanceRepository;

    private final CounterRepository counterRepository;

    private final ServiceRepository serviceRepository;

    private final EmployeeRepository employeeRepository;
    private final RoleRepository rolesRepository;

    private final BreakSessionRepository breakSessionRepository;
    private final QueueConfigurationRepository queueConfigurationRepository;
    private final SubTransactionRepository subTransactionRepository;
    private final SessionRepository sessionRepository;

    private LocalDate start;
    private LocalDate end;
    private Long totalTime;
    private Long totalBreakTime;
    private int breakCount;

    public Report getLiveReport(){
        return getHomeReport(LocalDate.now(),LocalDate.now());
    }

    public Report getHomeReport(LocalDate from,LocalDate to){
        LocalDateTime start = LocalDateTime.of(from,LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(to,LocalTime.MAX);
        Set<Assistance> assistances = assistanceRepository.findByCreatedAtBetweenAndOptionalBranch(start,end,null);
        return new Report(assistances,start.toLocalDate(),end.toLocalDate(),Type.DAILY_LIVE_REPORT,"");
    }
    public Report getHomeReportByBranch(LocalDate from,LocalDate to, String branchKey){
        LocalDateTime start = LocalDateTime.of(from,LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(to,LocalTime.MAX);
        Set<Assistance> assistances = assistanceRepository.findByCreatedAtBetweenAndBranch(start,end,branchKey, Sort.by(Sort.Order.asc("createdAt")));
        return new Report(assistances,start.toLocalDate(),end.toLocalDate(),Type.DAILY_LIVE_REPORT,"");
    }

    public Report getHomeReportByBranches(LocalDate from,LocalDate to, Set<String> branchKey){
        LocalDateTime start = LocalDateTime.of(from,LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(to,LocalTime.MAX);
        Set<Assistance> assistances = assistanceRepository.findByCreatedAtBetweenAndOptionalBranch(start,end,new ArrayList<>(branchKey));
        return new Report(assistances,start.toLocalDate(),end.toLocalDate(), Type.DAILY_LIVE_REPORT,"");
    }

    public Report getServiceReport(String serviceId, LocalDate from, LocalDate to){
        LocalDateTime start = LocalDateTime.of(from,LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(to,LocalTime.MAX);
        Set<Assistance> assistances = assistanceRepository.findByCreatedAtBetweenAndSessionsServiceIdAndOptionalBranch(start, end, serviceId, null);
        assistances.addAll(assistanceRepository.findByCreatedAtBetweenAndServiceIdAndOptionalBranch(start,end,serviceId,null));
        return new Report(assistances,start.toLocalDate(),end.toLocalDate(),Type.SERVICE,serviceId);
    }

    public Report getServiceReportByBranch(io.queberry.que.service.Service service, LocalDate from, LocalDate to, String branchKey){
        LocalDateTime start = LocalDateTime.of(from,LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(to,LocalTime.MAX);
        Set<Assistance> assistance = assistanceRepository.findByCreatedAtBetweenAndSessionsServiceIdAndOptionalBranch(start,end,service.getId(),Collections.singletonList(branchKey));
        assistance.addAll(assistanceRepository.findByCreatedAtBetweenAndServiceIdAndOptionalBranch(start,end,service.getId(),Collections.singletonList(branchKey)));
        return new Report(assistance,start.toLocalDate(),end.toLocalDate(),Type.SERVICE,service.getId());
    }

    public Report getServiceReportByBranches(io.queberry.que.service.Service service, LocalDate from, LocalDate to, Set<String> branchKey){
        LocalDateTime start = LocalDateTime.of(from,LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(to,LocalTime.MAX);
        Set<Assistance> assistance = assistanceRepository.findByCreatedAtBetweenAndSessionsServiceIdAndOptionalBranch(start,end,service.getId(), new ArrayList<>(branchKey));
        assistance.addAll(assistanceRepository.findByCreatedAtBetweenAndServiceIdAndOptionalBranch(start,end,service.getId(),new ArrayList<>(branchKey)));
        return new Report(assistance,start.toLocalDate(),end.toLocalDate(),Type.SERVICE,service.getId());
    }

    public Report getServiceReportByEmp(io.queberry.que.service.Service service, LocalDateTime start, LocalDateTime end, String emp){
        Set<Assistance> assistances = assistanceRepository.findByCreatedAtBetweenAndSessionsEmployeeAndSessionsServiceId(start,end,emp,service.getId());
        assistances.addAll(assistanceRepository.findByCreatedAtBetweenAndServiceIdAndOptionalBranch(start,end,service.getId(),null));
        return new Report(assistances,start.toLocalDate(),end.toLocalDate(),Type.SERVICE,service.getId());
    }

    public Report getSubServiceReportByBranch(String subServiceId, LocalDate from, LocalDate to, String branchKey){
        LocalDateTime start = LocalDateTime.of(from,LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(to,LocalTime.MAX);
        Set<Assistance> assistances = assistanceRepository.findByCreatedAtBetweenAndSessionsSubTransactionIdAndOptionalBranch(start,end,subServiceId,branchKey);
        return new Report(assistances,start.toLocalDate(),end.toLocalDate(),Type.SUB_SERVICE,subServiceId);
    }

    public Report getCounterReport(String counterId, LocalDate from, LocalDate to){
        LocalDateTime start = LocalDateTime.of(from,LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(to,LocalTime.MAX);
        Set<Assistance> assistances = assistanceRepository.findByCreatedAtBetweenAndSessionsCounterIdAndOptionalBranch(start,end,counterId,null);
        return new Report(assistances,start.toLocalDate(),end.toLocalDate(),Type.COUNTER,counterId);
    }

    public Report getCounterReportByBranch(Counter counter, LocalDate from, LocalDate to, String branchKey){
        LocalDateTime start = LocalDateTime.of(from,LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(to,LocalTime.MAX);
        Set<Assistance> assistances = assistanceRepository.findByCreatedAtBetweenAndSessionsCounterIdAndOptionalBranch(start,end,counter.getId(), branchKey);
        return new Report(assistances,start.toLocalDate(),end.toLocalDate(),Type.COUNTER,counter.getId());
    }

    public Report getEmployeeReportByBranch(Employee employee, LocalDate from, LocalDate to, String branchKey){
        LocalDateTime start = LocalDateTime.of(from,LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(to,LocalTime.MAX);
        Set<Assistance> assistances = assistanceRepository.findByCreatedAtBetweenAndSessionsEmployeeAndOptionalBranch(start,end,employee.getId(), branchKey);
        return new Report(assistances,start.toLocalDate(),end.toLocalDate(),Type.EMPLOYEE,employee.getId(),getEmpTotalTime(employee, start,end), getEmpBreakTime(employee.getId(), start, end), this.breakCount);
    }
    public CounterReport getAllCountersReport(LocalDate from, LocalDate to, ReportRequest.Parties parties){
        CounterReport counterReport = new CounterReport();
        List<Counter> counters = new ArrayList<>(0);
        if (parties.isAll())
            counters.addAll(counterRepository.findAll());
        else
            counters.addAll(counterRepository.findByIdIn(parties.getIds()));
        counters.forEach(counter -> {
            counterReport.add(new CounterReport.CounterLineItem(counter,getCounterReport(counter.getId(),from,to)));
        });
        return counterReport;
    }

    public CounterReport getAllCountersReportByBranch(LocalDate from, LocalDate to, ReportRequest.Parties parties, String branchId){
        CounterReport counterReport = new CounterReport();
        Optional<Branch> b = branchRepository.findById(branchId);
        List<Counter> counters = new ArrayList<>(0);
        if (parties.isAll())
            counters.addAll(counterRepository.findAllByBranch(b.get()));
        else
            counters.addAll(counterRepository.findByIdInAndBranch(parties.getIds(), b.get()));
        counters.forEach(counter -> {
            counterReport.add(new CounterReport.CounterLineItem(counter,getCounterReport(counter.getId(),from,to)));
        });
        return counterReport;
    }

    public CounterReport getCounterForBranches(LocalDate from, LocalDate to, Set<Branch> branches){
        CounterReport counterReport = new CounterReport();
        branches.forEach(branch -> {
            List<Counter> counters = new ArrayList<>(0);
            counters.addAll(counterRepository.findAllByBranch(branch));
            counters.forEach(counter -> {
                counterReport.add(new CounterReport.CounterLineItem(counter,getCounterReportByBranch(counter,from,to, branch.getBranchKey())));
            });
        });
        return counterReport;
    }

    public int getWeeklyStats(Set<String> branchSet, LocalDate date) {
            LocalDateTime start = LocalDateTime.of(date,LocalTime.MIN);
            LocalDateTime end = LocalDateTime.of(date,LocalTime.MAX);
            Set<Assistance> assistances = assistanceRepository.findByCreatedAtBetweenAndOptionalBranch(start,end,new ArrayList<>(branchSet));
            return assistances.size();
        }

    public int getCtrWeeklyStats(Counter c, LocalDate date) {
        LocalDateTime start = LocalDateTime.of(date,LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(date,LocalTime.MAX);
        Set<Assistance> assistances = assistanceRepository.findByCreatedAtBetweenAndSessionsCounterIdAndOptionalBranch(start, end,c.getId(),null );
        return assistances.size();
    }

    public ServiceReport getAllServicesReportByEmp(LocalDateTime from, LocalDateTime to, String empId, List<io.queberry.que.service.Service> services){
        Optional<Employee> employee = employeeRepository.findEmployeeById(empId);

        ServiceReport serviceReport = new ServiceReport();
        services.forEach(service -> {
//           / serviceReport.add(new ServiceReport.ServiceLineItem(service,getServiceReportByEmp(service,from,to, String.valueOf(employee.get())),0));
        });
        return serviceReport;
    }

    //home
    public ServiceReport getAllServicesReport(LocalDate from, LocalDate to, ReportRequest.Parties parties){
        List<String> services = new ArrayList<>(0);
        if (parties.isAll()) {
//            services.addAll(serviceRepository.findAll());
        }
        else {
            services.addAll(serviceRepository.findByIdIn(parties.getIds()));
        }
        ServiceReport serviceReport = new ServiceReport();
        services.forEach(service -> {
            serviceReport.add(new ServiceReport.ServiceLineItem(service,getServiceReport(service,from,to),0));
        });
        return serviceReport;
    }

    public ServiceReport getAllServicesReportByBranch(LocalDate from, LocalDate to, ReportRequest.Parties parties, String branchKey, String regionId){
        List<io.queberry.que.service.Service> services = new ArrayList<>(0);
        if (parties.isAll()) {
            services.addAll(serviceRepository.findAllByRegion(regionId));
        }
        else {
            services.addAll(serviceRepository.findByIdInAndRegion(parties.getIds(), regionId));
        }

        ServiceReport serviceReport = new ServiceReport();
        services.forEach(service -> {
//            serviceReport.add(new ServiceReport.ServiceLineItem(service,getServiceReportByBranch(service,from,to,branchKey),0));
        });
        return serviceReport;
    }

//    public ServiceReport getAllServicesForDashboard(LocalDate from, LocalDate to, String branchKey){
//        Branch branch = branchRepository.findByBranchKey(branchKey);
//        Set<Branch> branches = new HashSet<>();
//        branches.add(branch);
//        List<io.queberry.que.service.Service> services = new ArrayList<>();
//        services.addAll(branch.getServices());
//        ServiceReport serviceReport = new ServiceReport();
//
//        QueueConfiguration qc;
//        Optional<QueueConfiguration> queueConfiguration = queueConfigurationRepository.findByBranchKey(branchKey);
//        if(queueConfiguration.isPresent())
//            qc = queueConfiguration.get();
//        else
//            qc = queueConfigurationRepository.findByBranchKey(branchKey.substring(0,9)).get();
//
//        services.forEach(service -> {
//            Set<io.queberry.que.service.Service> serviceSet = new HashSet<>();
//            serviceSet.add(service);
//            Set<Counter> counters = new HashSet<>(0);
//            Set<Employee> employees = new HashSet<>(0);
//            if((qc.getServicePriority().equals(QueueConfiguration.ServicePriority.BOTH)) || (qc.getServicePriority().equals(QueueConfiguration.ServicePriority.COUNTER))) {
//                counters = counterRepository.findByBranchAndInUseAndFirstIsInOrSecondIsInOrThirdIsInOrFourthIsIn(branch, true, serviceSet, serviceSet, serviceSet, serviceSet);
//            }
//            if((qc.getServicePriority().equals(QueueConfiguration.ServicePriority.BOTH)) || (qc.getServicePriority().equals(QueueConfiguration.ServicePriority.USER))) {
//                employees = employeeRepository.findByBranchesInAndLoggedCounterIsNotNullAndServicesIsInOrSecondIsInOrThirdIsInOrFourthIsIn(branches, serviceSet, serviceSet, serviceSet, serviceSet);
//            }
//            serviceReport.add(new ServiceReport.ServiceLineItem(service,getServiceReportByBranch(service,from,to,branchKey),counters.size() + employees.size()));
//        });
//        return serviceReport;
//    }
//
//    public ServiceReport getAllServicesForBranches(LocalDate from, LocalDate to, Set<Branch> branches, Set<String> branchKeys){
//        Set<io.queberry.que.service.Service> services = new HashSet<>();
//        branches.forEach(branch -> {
//            services.addAll(branch.getServices());
//        });
//
//        ServiceReport serviceReport = new ServiceReport();
//        QueueConfiguration qc;
//
//        if (branches.size() == 1) {
//            String branchKey = branchKeys.stream().findFirst().orElse(null);
//
//            if (branchKey != null) {
//                qc = queueConfigurationRepository.findByBranchKey(branchKey)
//                        .orElseGet(() -> queueConfigurationRepository.findByBranchKey(branchKey.substring(0, 9))
//                                .orElseGet(() -> queueConfigurationRepository.findByBranchKey(null).orElse(null)));
//            } else {
//                qc = null;
//            }
//        }
//        else {
//            qc = null;
//        }
//        services.forEach(service -> {
//            Set<io.queberry.que.service.Service> serviceSet = new HashSet<>();
//            serviceSet.add(service);
//            Set<Counter> counters = new HashSet<>(0);
//            Set<Employee> employees = new HashSet<>(0);
//            if(qc != null) {
//                if ((qc.getServicePriority().equals(QueueConfiguration.ServicePriority.BOTH)) || (qc.getServicePriority().equals(QueueConfiguration.ServicePriority.COUNTER))) {
//                    counters = counterRepository.findByBranchAndInUseAndFirstIsInOrSecondIsInOrThirdIsInOrFourthIsIn(branches.stream().findFirst().get(), true, serviceSet, serviceSet, serviceSet, serviceSet);
//                }
//                if ((qc.getServicePriority().equals(QueueConfiguration.ServicePriority.BOTH)) || (qc.getServicePriority().equals(QueueConfiguration.ServicePriority.USER))) {
//                    employees = employeeRepository.findByBranchesInAndLoggedCounterIsNotNullAndServicesIsInOrSecondIsInOrThirdIsInOrFourthIsIn(branches, serviceSet, serviceSet, serviceSet, serviceSet);
//                }
//            }
//            serviceReport.add(new ServiceReport.ServiceLineItem(service,getServiceReportByBranches(service,from,to,branchKeys),counters.size() + employees.size()));
//        });
//        return serviceReport;
//    }

    public EmployeeReport getAllEmployeesReportByBranch(LocalDate from, LocalDate to, ReportRequest.Parties parties,String branchKey){
        List<Employee> employees = new ArrayList<>(0);
        if (parties.isAll())
            employees.addAll(employeeRepository.findAll());
        else
            employees.addAll(employeeRepository.findAllById(parties.getIds()));
        EmployeeReport employeeReport = new EmployeeReport();
        employees.forEach(employee -> {
            employeeReport.add(new EmployeeReport.EmployeeLineItem(employee,getEmployeeReportByBranch(employee,from,to, branchKey)));
        });
        return employeeReport;
    }

//    public ReportController.EmpDashboardRes getEmpDashboard(LocalDateTime from, LocalDateTime to, Employee employee, String branchKey, QueueConfiguration.ServicePriority priority){
//        ReportController.EmpDashboardRes empDashboardRes = new ReportController.EmpDashboardRes();
//        ModelMapper modelMapper = new ModelMapper();
//        ReportController.EmpInfo empInfo = modelMapper.map(employee, ReportController.EmpInfo.class);
//        Counter counter = null;
//        empDashboardRes.setEmployee(empInfo);
//        if(employee.getCounter() != null) {
//            counter = employee.getCounter();
//            ReportController.CounterInfo counterInfo = modelMapper.map(counter, ReportController.CounterInfo.class);
//            empDashboardRes.setCounter(counterInfo);
//        }
//        try {
//            Set<io.queberry.que.service.Service> serviceSet = new HashSet<>(0);
//            Set<String> services = new HashSet<>();
//            if((priority.equals(QueueConfiguration.ServicePriority.BOTH)) || (priority.equals(QueueConfiguration.ServicePriority.USER))) {
//                serviceSet.addAll(employee.getServices());
//                serviceSet.addAll(employee.getSecond());
//                serviceSet.addAll(employee.getThird());
//                serviceSet.addAll(employee.getFourth());
//                services.addAll(employee.getServices().stream().map(io.queberry.que.service.Service::getId).collect(Collectors.toSet()));
//                services.addAll(employee.getSecond().stream().map(io.queberry.que.service.Service::getId).collect(Collectors.toSet()));
//                services.addAll(employee.getThird().stream().map(io.queberry.que.service.Service::getId).collect(Collectors.toSet()));
//                services.addAll(employee.getFourth().stream().map(io.queberry.que.service.Service::getId).collect(Collectors.toSet()));
//            }
//            if(employee.getLoggedCounter() != null) {
//                Optional<Counter> ctr = counterRepository.findCounterById(employee.getLoggedCounter());
//                if(ctr.isPresent()) {
//                    counter = ctr.get();
//                    ReportController.CounterInfo counterInfo = modelMapper.map(counter, ReportController.CounterInfo.class);
//                    empDashboardRes.setCounter(counterInfo);
//                    if((priority.equals(QueueConfiguration.ServicePriority.BOTH)) || (priority.equals(QueueConfiguration.ServicePriority.COUNTER))) {
//                        serviceSet.addAll(counter.getFirst());
//                        serviceSet.addAll(counter.getSecond());
//                        serviceSet.addAll(counter.getThird());
//                        serviceSet.addAll(counter.getFourth());
//                        services.addAll(counter.getFirst().stream().map(io.queberry.que.service.Service::getId).collect(Collectors.toSet()));
//                        services.addAll(counter.getSecond().stream().map(io.queberry.que.service.Service::getId).collect(Collectors.toSet()));
//                        services.addAll(counter.getThird().stream().map(io.queberry.que.service.Service::getId).collect(Collectors.toSet()));
//                        services.addAll(counter.getFourth().stream().map(io.queberry.que.service.Service::getId).collect(Collectors.toSet()));
//                    }
//                }
//            }
//            Set<Assistance> assistances = assistanceRepository.findByCreatedAtBetweenAndStatusAndBranchAndServiceIdIn(from, to,Status.SCHEDULED,branchKey,services);
//            empDashboardRes.setNoOfWaiting(assistances.size());
//            Set<Session> empSessions = sessionRepository.findByEmployeeAndServiceIdInAndCreatedAtBetween(employee.getId(), services, from, to);
//
//            empDashboardRes.setNoOfTransferred(empSessions.size());
//
//            Optional<Assistance> assistance = assistanceRepository.findByCreatedAtBetweenAndSessionsOngoingAndSessionsEmployeeAndBranch(from, to, true, employee.getId(), branchKey);
//            if(assistance.isPresent()) {
//                empDashboardRes.setOngoingToken(assistance.get().getToken());
//                empDashboardRes.setServiceName(assistance.get().getService().getName());
//            }
//        }
//        catch (Exception e){
//            log.info("exception:{}", e.getMessage());
//        }
//        return empDashboardRes;
//    }


    public SubServiceReport getAllSubServicesReportByBranch(LocalDate from, LocalDate to, ReportRequest.Parties parties, String branchKey){
//        List<SubTransaction> subServices = new ArrayList<>(0);
//        if (parties.isAll())
//            subServices.addAll(subTransactionRepository.findAll());
//        else
//            subServices.addAll(subTransactionRepository.findByIdIn(parties.getIds()));
//
//        SubServiceReport subServiceReport = new SubServiceReport();
//        subServices.forEach(subService -> {
//            subServiceReport.add(new SubServiceReport.SubServiceLineItem(subService,getSubServiceReportByBranch(subService.getId(),from,to, branchKey)));
//        });
//        return subServiceReport;
        return null;
    }

    public Report getAllDetails(LocalDateTime from, LocalDateTime to, Region region, String branchKey){

        Set<String> branchKeys = new HashSet<>();
//
//        if (branchKey != null) {
//            branchKeys.add(branchKey);
//        } else if(region != null) {
//            Set<Branch> branches = branchRepository.findByRegionAndActiveTrue(region);
//            if(branches.size() > 0) {
//                branches.forEach(branch -> {
//                    branchKeys.add(branch.getBranchKey());
//                });
//            }
//        } else {
//            Set<Branch> branches = branchRepository.findByActiveTrue();
//            branches.forEach(branch -> {
//                branchKeys.add(branch.getBranchKey());
//            });
//        }
//
////        branchKeys.forEach(branch ->{
////            log.info("branch: {}", branch);
////        });
//
//        if(branchKeys.size() > 0){
//            Set<Assistance> assistances = assistanceRepository.findByCreatedAtBetweenAndOptionalBranch(from, to, new ArrayList<>(branchKeys));
//            return (new Report(assistances,from.toLocalDate(),to.toLocalDate(),Type.DAILY_LIVE_REPORT,""));
//        } else {
//            Set<Assistance> assistances = new HashSet<>();
//            return (new Report(assistances,from.toLocalDate(),to.toLocalDate(),Type.DAILY_LIVE_REPORT,""));
//        }
        return null;
    }

    public Long getEmpBreakTime(String party, LocalDateTime from, LocalDateTime to){

//        this.totalBreakTime = 0L;
//        this.breakCount = 0;
//        List<BreakSessionDTO> breakSessions = breakSessionRepository.findByStartTimeBetweenAndEmployeeId(party,from, to);
//        if(breakSessions.size() > 0){
//            breakSessions.forEach(breakSession -> {
//                if(breakSession.getEndTime() != null) {
//                    this.totalBreakTime += Duration.between(breakSession.getStartTime(), breakSession.getEndTime()).getSeconds();
//                }
//            });
//         this.breakCount = breakSessions.size();
//        }
//        return this.totalBreakTime;
        return 0L;
    }

    public Long getEmpTotalTime(String party, LocalDateTime from, LocalDateTime to) {
        Home home = homeRepository.findAll().get(0);
        Employee employee = employeeRepository.findEmployeeById(party).get();
        if (employee.getAuthorities().stream().findFirst().get().getName().equals("COUNTER_AGENT")) {
            Set<EmployeeSessions> employeeSessions = employeeSessionRepository.findByEmployeeAndLoginTimeBetweenOrderByLoginTimeAsc(employee, from,to);
            this.totalTime=0L;

            employeeSessions.forEach(employeeSessions1 -> {
                this.start = LocalDate.from(employeeSessions1.getLoginTime());
                if (employeeSessions1.getLogoutTime() != null) {
                    this.end = LocalDate.from(employeeSessions1.getLogoutTime());

                    if (this.start.equals(this.end)) {
                        this.totalTime += Duration.between(employeeSessions1.getLoginTime(),employeeSessions1.getLogoutTime()).getSeconds();
                    }
                    else if (this.end.isAfter(this.start)) {
                        LocalDateTime ld = employeeSessions1.getLoginTime();
                        ld = ld.withHour(home.getBusinessEnd().getHour());
                        ld = ld.withMinute(home.getBusinessEnd().getMinute());
                        ld = ld.withSecond(home.getBusinessEnd().getSecond());

                        this.totalTime += Duration.between(employeeSessions1.getLoginTime(), ld).getSeconds();
                    }
                }
                else
                    this.totalTime += Duration.between(employeeSessions1.getLoginTime(), LocalDateTime.now()).getSeconds();
            });
            return (this.totalTime);
        }
        else return 0L;
    }

    public Long getEmpTotalTime(Employee employee, LocalDateTime from, LocalDateTime to) {
        log.info("emp name:{}", employee.getUsername());
        try {
            if (employee.getAuthorities().contains(rolesRepository.findByName("COUNTER_AGENT"))) {
                List<EmployeeSessionsDTO> employeeSessions = employeeSessionRepository.findByLoginTimeBetweenAndEmployeeIdOrderByLoginTimeAsc(employee.getId(), from,to);
                this.totalTime=0L;

                employeeSessions.forEach(employeeSessions1 -> {
                    this.start = LocalDate.from(employeeSessions1.getLoginTime());
                    if (employeeSessions1.getLogoutTime() != null) {
                        this.end = LocalDate.from(employeeSessions1.getLogoutTime());
                        this.totalTime += Duration.between(employeeSessions1.getLoginTime(),employeeSessions1.getLogoutTime()).getSeconds();
                    }
                    else {
                        if (LocalDate.now().isAfter(this.start)) {
                            LocalDateTime ld = employeeSessions1.getLoginTime();
                            ld = ld.withHour(to.getHour());
                            ld = ld.withMinute(to.getMinute());
                            ld = ld.withSecond(to.getSecond());
                            this.totalTime += Duration.between(employeeSessions1.getLoginTime(), ld).getSeconds();
                        } else {
                            this.totalTime += Duration.between(employeeSessions1.getLoginTime(), LocalDateTime.now()).getSeconds();
                        }
                    }
                });
                return (this.totalTime);
            }
            else return 0L;
        }
        catch (Exception e){
            log.info(e.getMessage());
            return 0L;
        }
    }

}

