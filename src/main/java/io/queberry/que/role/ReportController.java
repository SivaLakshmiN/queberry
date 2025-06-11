//package io.queberry.que.role;
//
//import io.queberry.que.assistance.Assistance;
//import io.queberry.que.assistance.AssistanceRepository;
//import io.queberry.que.branch.Branch;
//import io.queberry.que.branch.BranchRepository;
//import io.queberry.que.config.JwtTokenUtil;
//import io.queberry.que.config.Queue.QueueConfigurationRepository;
//import io.queberry.que.counter.Counter;
//import io.queberry.que.counter.CounterRepository;
//import io.queberry.que.employee.Employee;
//import io.queberry.que.employee.EmployeeRepository;
//import io.queberry.que.enums.Type;
//import io.queberry.que.region.Region;
//import io.queberry.que.region.RegionRepository;
//import io.queberry.que.service.Service;
//import io.queberry.que.subService.SubService;
//import io.queberry.que.subService.SubServiceRepository;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.*;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@RequiredArgsConstructor
//@RequestMapping("/api/reporting")
//@Slf4j
//public class ReportController {
//
//    private final ReportingBean reportingBean;
//
//    private final ReportingService reportingService;
//
//    private final NonAggregatedReportService nonAggregatedReportService;
//
//    private final CounterRepository counterRepository;
//
//    private final EmployeeRepository employeeRepository;
//
//    private final SubServiceRepository subServiceRepository;
//
//    private final AssistanceRepository assistanceRepository;
//    private final BranchRepository branchRepository;
//    private final QueueConfigurationRepository queueConfigurationRepository;
//    private final RegionRepository regionRepository;
//    private final RoleRepository rolesRepository;
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//    //    @Scheduled(fixedRate = 60000)
////    @PutMapping("/report/live")
////    public ResponseEntity liveReport(HttpServletRequest request, @RequestBody ReportRequest reportRequest) {
////        String token = request.getHeader("Authorization").substring(7);
////        if(jwtTokenUtil.isTokenExpired(token)){
////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT token expired!!");
////        }
////        String username = jwtTokenUtil.getUsernameFromToken(token);
////        Employee employee = employeeRepository.findByUsername(username);
////        Set<Role> roles = employee.getRoles();
////        Set<String> branches = new HashSet<>();
////        Set<Branch> branchSet = new HashSet<>();
////        Set<Branch> managedBranches = new HashSet<>();
////        if (reportRequest.isFilter()) {
////            if (reportRequest.getBranch() != null) {
////                branches.add(reportRequest.getBranch().getBranchKey());
////                branchSet.add(reportRequest.getBranch());
////            } else {
//////                branchSet = branchRepository.findByRegion(reportRequest.getRegion());
////                branchSet = employee.getBranches();
////                branchSet.forEach(branch -> {
////                    branches.add(branch.getBranchKey());
////                });
////            }
////            managedBranches = branchSet.stream()
////                    .map(branch -> branchRepository.findById(branch.getId()).orElse(branch))
////                    .collect(Collectors.toSet());
////            reportingBean.setLive(reportingService.getHomeReportByBranches(LocalDate.now(), LocalDate.now(), branches));
////            if(managedBranches.size() > 0) {
////                reportingBean.setCounterReport(reportingService.getCounterForBranches(LocalDate.now(), LocalDate.now(), managedBranches));
////            }
////            reportingBean.setServiceReport(reportingService.getAllServicesForBranches(LocalDate.now(), LocalDate.now(), managedBranches, branches));
////        } else {
////            if(roles.contains(rolesRepository.findByName("PRODUCT_ADMIN")) || roles.contains(rolesRepository.findByName("ORG_ADMIN"))) {
////                ReportRequest.Parties parties = new ReportRequest.Parties();
////                parties.setAll(true);
////                reportingBean.setLive(reportingService.getLiveReport());
////               // reportingBean.setCounterReport(reportingService.getAllCountersReport(LocalDate.now(), LocalDate.now(), parties));
////                reportingBean.setServiceReport(reportingService.getAllServicesReport(LocalDate.now(), LocalDate.now(), parties));
////            } else {
////                branchSet = employee.getBranches();
////                branchSet.forEach(branch -> {
////                    branches.add(branch.getBranchKey());
////                });
////
////            managedBranches = branchSet.stream()
////                    .map(branch -> branchRepository.findById(branch.getId()).orElse(branch))
////                    .collect(Collectors.toSet());
////            reportingBean.setLive(reportingService.getHomeReportByBranches(LocalDate.now(), LocalDate.now(), branches));
////            if(managedBranches.size() >0) {
////                reportingBean.setCounterReport(reportingService.getCounterForBranches(LocalDate.now(), LocalDate.now(), managedBranches));
////            }
////            reportingBean.setServiceReport(reportingService.getAllServicesForBranches(LocalDate.now(), LocalDate.now(), managedBranches, branches));
////             }
////            }
////        return ResponseEntity.ok(reportingBean);
////    }
////
////    @GetMapping("/report/live")
////    public ResponseEntity liveReportForDevice(HttpServletRequest request){
////        log.info("request header in transferToUser {}",request.getHeader("X-TenantID"));
////        reportingBean.setServiceReport(reportingService.getAllServicesForDashboard(LocalDate.now(),LocalDate.now(),request.getHeader("X-TenantID")));
////        return ResponseEntity.ok(reportingBean);
////    }
//
//    @PutMapping("/report")
//    public ResponseEntity<?> report(@RequestBody ReportRequest reportRequest) {
//        if (reportRequest.isAggregate()) {
//            switch (reportRequest.getType()) {
//                case DAILY_LIVE_REPORT:
//                    return ResponseEntity.ok(reportingService.getHomeReportByBranch(reportRequest.getFrom(), reportRequest.getTo(), reportRequest.getBranch().getBranchKey()));
//                case COUNTER:
//                    return ResponseEntity.ok(reportingService.getAllCountersReportByBranch(reportRequest.getFrom(), reportRequest.getTo(), reportRequest.getParties(), reportRequest.getBranch().getId()));
//                case SERVICE:
//                    return ResponseEntity.ok(reportingService.getAllServicesReportByBranch(reportRequest.getFrom(), reportRequest.getTo(), reportRequest.getParties(), reportRequest.getBranch().getBranchKey(), reportRequest.getRegion()));
//                case EMPLOYEE:
//                    return ResponseEntity.ok(reportingService.getAllEmployeesReportByBranch(reportRequest.getFrom(), reportRequest.getTo(), reportRequest.getParties(), reportRequest.getBranch().getBranchKey()));
//                case SUB_SERVICE:
//                    return ResponseEntity.ok(reportingService.getAllSubServicesReportByBranch(reportRequest.getFrom(), reportRequest.getTo(), reportRequest.getParties(), reportRequest.getBranch().getBranchKey()));
//            }
//        } else {
//            switch (reportRequest.getType()) {
//                case DAILY_LIVE_REPORT:
//                    return ResponseEntity.ok(nonAggregatedReportService.getHomeReportByBranch(reportRequest.getFrom(), reportRequest.getTo(), reportRequest.getBranch().getBranchKey()));
//                case COUNTER:
//                    return ResponseEntity.ok(nonAggregatedReportService.getAllCountersReportByBranch(reportRequest.getFrom(), reportRequest.getTo(), reportRequest.getParties(), reportRequest.getBranch().getId()));
//                case SERVICE:
//                    return ResponseEntity.ok(nonAggregatedReportService.getAllServicesReportByBranch(reportRequest.getFrom(), reportRequest.getTo(), reportRequest.getParties(), reportRequest.getBranch().getBranchKey(), reportRequest.getRegion()));
//                case EMPLOYEE:
//                    return ResponseEntity.ok(nonAggregatedReportService.getAllEmployeesReportByBranch(reportRequest.getFrom(), reportRequest.getTo(), reportRequest.getParties(), reportRequest.getBranch().getBranchKey()));
//                case SUB_SERVICE:
//                    return ResponseEntity.ok(nonAggregatedReportService.getAllSubServicesReportByBranch(reportRequest.getFrom(), reportRequest.getTo(), reportRequest.getParties(), reportRequest.getBranch().getBranchKey()));
//
//            }
//        }
//        return ResponseEntity.badRequest().build();
//    }
//
////    @PutMapping("/report/regionBranchDonut")
////    public ResponseEntity<?> getRegionDetails(HttpServletRequest request, @RequestBody ReportRequest reportRequest) {
////        String token = request.getHeader("Authorization").substring(7);
////        if(jwtTokenUtil.isTokenExpired(token)){
////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT token expired!!");
////        }
////        String username = jwtTokenUtil.getUsernameFromToken(token);
////        Employee employee = employeeRepository.findByUsername(username);
////        Set<Role> roles = employee.getRoles();
////
////        LocalDateTime start = LocalDateTime.of(reportRequest.getFrom(), LocalTime.MIN);
////        LocalDateTime end = LocalDateTime.of(reportRequest.getTo(), LocalTime.MAX);
////        Set<ReportRes> reportResSet = new HashSet<>();
////
////        if(roles.contains(rolesRepository.findByName("PRODUCT_ADMIN")) || roles.contains(rolesRepository.findByName("ORG_ADMIN"))) {
////            if (reportRequest.getType().equals(Type.REGION) && (!reportRequest.isAggregate())) {
////                List<Region> regionSet = regionRepository.findAll();
////                regionSet.forEach(region -> {
//////                    log.info("region: {}", region.getId());
////                    ReportRes reportRes = new ReportRes();
////                    reportResSet.add(reportRes);
////                });
////            } else if (reportRequest.getType().equals(Type.REGION)) {
////                ReportRes reportRes = new ReportRes();
////                reportRes.setReport(reportingService.getAllDetails(start, end, null, null));
////                reportResSet.add(reportRes);
////            } else if (reportRequest.getType().equals(Type.BRANCH) && (!reportRequest.isAggregate())) {
//////                log.info("branch data");
//////                Set<Branch> branches = branchRepository.findByRegion(reportRequest.getRegion());
//////                branches.forEach(branch -> {
////////                    log.info("branch: {}", branch.getName());
//////                    ReportRes reportRes = new ReportRes(branch.getBranchKey(), branch.getName(), reportRequest.getRegion(), reportingService.getAllDetails(start, end, reportRequest.getRegion(), branch.getBranchKey()));
//////                    reportResSet.add(reportRes);
////                });
////            } else if (reportRequest.getType().equals(Type.BRANCH)) {
////                ReportRes reportRes = new ReportRes();
////                reportRes.setReport(reportingService.getAllDetails(start, end, null, null));
////                reportResSet.add(reportRes);
////            }
////        } else {
////            if (reportRequest.getType().equals(Type.REGION) && (!reportRequest.isAggregate())) {
////                String region = employee.getRegion();
////                List<String> regionSet = new ArrayList<>();
////                regionSet.add(region);
////                regionSet.forEach(r -> {
//////                    log.info("region: {}", r.getId());
////                    ReportRes reportRes = new ReportRes(r, r, r, reportingService.getAllDetails(start, end, r, null));
////                    reportResSet.add(reportRes);
////                });
////            } else if (reportRequest.getType().equals(Type.REGION)) {
////                ReportRes reportRes = new ReportRes();
////                reportRes.setReport(reportingService.getAllDetails(start, end, null, null));
////                reportResSet.add(reportRes);
////            } else if (reportRequest.getType().equals(Type.BRANCH) && (!reportRequest.isAggregate())) {
//////                log.info("branch data");
////                Set<String> branches = employee.getBranches();
////                branches.forEach(branch -> {
//////                    log.info("branch: {}", branch.getName());
////                    ReportRes reportRes = new ReportRes();
////                    reportResSet.add(reportRes);
////                });
////            } else if (reportRequest.getType().equals(Type.BRANCH)) {
////                ReportRes reportRes = new ReportRes();
////                reportRes.setReport(reportingService.getAllDetails(start, end, null, null));
////                reportResSet.add(reportRes);
////            }
////        }
////
////        return ResponseEntity.ok(reportResSet);
////    }
//
//    @PutMapping("/employeeReport")
//    public ResponseEntity<?> getEmployeeReport(@RequestBody EmpRequest reportRequest) {
//        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
//        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
//        return ResponseEntity.ok(reportingService.getAllServicesReportByEmp(start, end, reportRequest.getEmpId(), reportRequest.getServices()));
//    }
//
////    @GetMapping("/employeesDashboard")
////    public ResponseEntity<?> getEmployeesDashboard(HttpServletRequest request) {
////        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
////        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
////        String branchKey = request.getHeader("X-TenantId");
////        log.info("branchkey:{}", branchKey);
////        Branch branch = branchRepository.findByBranchKey(branchKey);
////        QueueConfiguration qc;
//////        Optional<QueueConfiguration> queueConfiguration = queueConfigurationRepository.findByBranchKey(branchKey);
//////        if (queueConfiguration.isPresent())
//////            qc = queueConfiguration.get();
//////        else
//////            qc = queueConfigurationRepository.findByBranchKey(branchKey.substring(0, 9)).get();
//////
////        qc = queueConfigurationRepository.findByBranchKey(branchKey)
////                .orElseGet(() -> queueConfigurationRepository.findByBranchKey(branchKey.substring(0, 9))
////                        .orElseGet(() -> queueConfigurationRepository.findByBranchKey(null).orElse(null)));
////
//////        Set<Branch> branches = new HashSet<>(1);
//////        branches.add(branch);
//////        Set<Employee> employeeSet = employeeRepository.findByBranchesIn(branches);
////        Set<Employee> employeeSet = employeeRepository.findByBranchesIdAndLoggedCounterIsNotNull(branch.getId());
////        Set<EmpDashboardRes> empDashboardResSet = new HashSet<>();
////        employeeSet.forEach(employee -> {
//////            log.info("employee: {}", employee.getId());
////            empDashboardResSet.add(reportingService.getEmpDashboard(start, end, employee, branchKey, qc!=null?qc.getServicePriority(): QueueConfiguration.ServicePriority.COUNTER));
////        });
////        return ResponseEntity.ok(empDashboardResSet);
////    }
//
//    @GetMapping("/sub-services")
//    public ResponseEntity<List<SubService>> getAllSubServices() {
//        List<Integer> l1 = new ArrayList<>();
//        return ResponseEntity.ok(subServiceRepository.findAll());
//    }
//
//    @PutMapping("/tokenReport")
//    public ResponseEntity<?> getTokenReport(@RequestBody ReportRequest reportRequest) {
//        LocalDateTime start = LocalDateTime.of(reportRequest.getFrom(), LocalTime.MIN);
//        LocalDateTime end = LocalDateTime.of(reportRequest.getTo(), LocalTime.MAX);
//        Sort sort = Sort.by(Sort.Order.asc("createdAt"));
//        Set<Assistance> assistances = assistanceRepository.findByCreatedAtBetweenAndBranch(start, end, reportRequest.getBranch().getBranchKey(), sort);
//        Set<Report> reports = new HashSet<>();
//
//        for (Assistance assistance : assistances) {
//            reports.add(new Report(assistance));
//        }
//        return ResponseEntity.ok(reports);
//    }
//
//    @PutMapping("/dashboard/weeklyStats")
//    public ResponseEntity getWeeklyStats(HttpServletRequest request, @RequestBody weeklyStatsRequest weeklyStatsRequest) {
//        String token = request.getHeader("Authorization").substring(7);
//        if(jwtTokenUtil.isTokenExpired(token)){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT token expired!!");
//        }
//        String username = jwtTokenUtil.getUsernameFromToken(token);
//        Employee employee = employeeRepository.findByUsername(username);
//        Set<Role> roles = employee.getRoles();
//        Set<Region> regions = new HashSet<>();
//        Set<String> branches = new HashSet<>();
//        Set<Counter> counters = new HashSet<>();
//        Set<weeklyStatsResponse> responses = new HashSet<>();
//        switch (weeklyStatsRequest.getType().name()) {
//            case "REGION": {
//                if (weeklyStatsRequest.getIds() != null) {
//                    if(roles.contains(rolesRepository.findByName("PRODUCT_ADMIN")) || roles.contains(rolesRepository.findByName("ORG_ADMIN"))) {
////                        Set<String> branchSet = branchRepository.findByRegion(regionRepository.findById(weeklyStatsRequest.getIds()).get()).toSet();
//                        Set<String> branchKeys = new HashSet<>();
////                        branchSet.forEach(branch -> {
////                            branchKeys.add(branch);
//                            LocalDate currentDate = LocalDate.now();
//                            Set<statCnt> statCntSet = new HashSet<>();
//                            for (int i = 0; i < 7; i++) {
//                                int cnt = reportingService.getWeeklyStats(branchKeys, currentDate.minusDays(i));
//                                statCnt statCnt = new statCnt(currentDate.minusDays(i), cnt);
//                                statCntSet.add(statCnt);
//                            }
//                            branchKeys.clear();
//                            responses.add(new weeklyStatsResponse());
//                    } else {
//                        branches.addAll(employee.getBranches());
//                        Set<String> branchKeys = new HashSet<>();
//                        branches.forEach(branch -> {
//                            branchKeys.add(branch);
//                            LocalDate currentDate = LocalDate.now();
//                            Set<statCnt> statCntSet = new HashSet<>();
//                            for (int i = 0; i < 7; i++) {
//                                int cnt = reportingService.getWeeklyStats(branchKeys, currentDate.minusDays(i));
//                                statCnt statCnt = new statCnt(currentDate.minusDays(i), cnt);
//                                statCntSet.add(statCnt);
//                            }
//                            branchKeys.clear();
//                            responses.add(new weeklyStatsResponse());
//                        });
//                    }
//                    return ResponseEntity.ok(responses);
//                } else {
//                    Set<Branch> branchSet = new HashSet<>(branchRepository.findAll());
//                    Set<String> branchKeys = new HashSet<>();
//                    branchSet.forEach(branch -> {
//                        branchKeys.add(branch.getBranchKey());
//                        LocalDate currentDate = LocalDate.now();
//                        Set<statCnt> statCntSet = new HashSet<>();
//                        for (int i = 0; i < 7; i++) {
//                            int cnt = reportingService.getWeeklyStats(branchKeys, currentDate.minusDays(i));
//                            statCnt statCnt = new statCnt(currentDate.minusDays(i), cnt);
//                            statCntSet.add(statCnt);
//                        }
//                        responses.add(new weeklyStatsResponse(branch.getName(), branch.getId(), statCntSet));
//                        branchKeys.clear();
//                    });
//                    return ResponseEntity.ok(responses);
//                }
//            }
//            case "BRANCH": {
//                if (weeklyStatsRequest.getIds() != null) {
//                    Branch branch = branchRepository.findByBranchKey(weeklyStatsRequest.getIds());
//                    Set<Counter> counterSet = new HashSet<>(counterRepository.findAllByBranch(branch));
//                    counterSet.forEach(counter -> {
//                        LocalDate currentDate = LocalDate.now();
//                        Set<statCnt> statCntSet = new HashSet<>();
//                        for (int i = 0; i < 7; i++) {
//                            int cnt = reportingService.getCtrWeeklyStats(counter, currentDate.minusDays(i));
//                            statCnt statCnt = new statCnt(currentDate.minusDays(i), cnt);
//                            statCntSet.add(statCnt);
//                        }
//                        responses.add(new weeklyStatsResponse(counter.getName(), counter.getId(), statCntSet));
//                    });
//                    return ResponseEntity.ok(responses);
//                } else {
//                    if(roles.contains(rolesRepository.findByName("PRODUCT_ADMIN")) || roles.contains(rolesRepository.findByName("ORG_ADMIN"))) {
//                        Set<Counter> counterSet = new HashSet<>(counterRepository.findAll());
//                        counterSet.forEach(counter -> {
//                            LocalDate currentDate = LocalDate.now();
//                            Set<statCnt> statCntSet = new HashSet<>();
//                            for (int i = 0; i < 7; i++) {
//                                int cnt = reportingService.getCtrWeeklyStats(counter, currentDate.minusDays(i));
//                                statCnt statCnt = new statCnt(currentDate.minusDays(i), cnt);
//                                statCntSet.add(statCnt);
//                            }
//                            responses.add(new weeklyStatsResponse(counter.getName(), counter.getId(), statCntSet));
//                        });
//                        return ResponseEntity.ok(responses);
//
//                    } else {
//                        Set<String> branch = employee.getBranches();
//                        Set<Counter> counterSet = new HashSet<>(counterRepository.findAllByBranchIn(branch));
//                        counterSet.forEach(counter -> {
//                            LocalDate currentDate = LocalDate.now();
//                            Set<statCnt> statCntSet = new HashSet<>();
//                            for (int i = 0; i < 7; i++) {
//                                int cnt = reportingService.getCtrWeeklyStats(counter, currentDate.minusDays(i));
//                                statCnt statCnt = new statCnt(currentDate.minusDays(i), cnt);
//                                statCntSet.add(statCnt);
//                            }
//                            responses.add(new weeklyStatsResponse(counter.getName(), counter.getId(), statCntSet));
//                        });
//                        return ResponseEntity.ok(responses);
//                    }
//                }
//            }
//            default: {
//                log.info("org dashboard");
//                if(roles.contains(rolesRepository.findByName("PRODUCT_ADMIN")) || roles.contains(rolesRepository.findByName("ORG_ADMIN"))) {
//                    regions = new HashSet<>(regionRepository.findAll());
//                    Set<String> branchKeys = new HashSet<>();
//                    Set<String> branchSet = new HashSet<>();
//                    regions.forEach(region -> {
////                        branchSet.addAll(branchRepository.findByRegion());
//                        if(branchSet.size() > 0){
//                            branchSet.forEach(branch -> {
//                                branchKeys.add(branch);
//                            });
//                            LocalDate currentDate = LocalDate.now();
//                            Set<statCnt> statCntSet = new HashSet<>();
//                            for (int i = 0; i < 7; i++) {
//                                int cnt = reportingService.getWeeklyStats(branchKeys, currentDate.minusDays(i));
//                                statCnt statCnt = new statCnt(currentDate.minusDays(i), cnt);
//                                statCntSet.add(statCnt);
//                            }
//                            responses.add(new weeklyStatsResponse(region.getName(), region.getId(), statCntSet));
//                            branchKeys.clear();
//                            branchSet.clear();
//                        }
//                    });
//                    return ResponseEntity.ok(responses);
//                } else {
//                    Set<String> branchKeys = new HashSet<>();
//                    branches.addAll(employee.getBranches());
//                    if(branches.size() > 0) {
//                        branches.forEach(branch -> {
//                            branchKeys.add(branch);
//                        });
//                        LocalDate currentDate = LocalDate.now();
//                        Set<statCnt> statCntSet = new HashSet<>();
//                        for (int i = 0; i < 7; i++) {
//                            int cnt = reportingService.getWeeklyStats(branchKeys, currentDate.minusDays(i));
//                            statCnt statCnt = new statCnt(currentDate.minusDays(i), cnt);
//                            statCntSet.add(statCnt);
//                        }
//                        responses.add(new weeklyStatsResponse());
//                    }
//                    return ResponseEntity.ok(responses);
//                }
//            }
//        }
//    }
//
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Getter
//    public static class weeklyStatsRequest {
//        private Type type;
//        private boolean filter;
//        private String ids;
//    }
//
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Setter
//    public static class weeklyStatsResponse {
//        private String name;
//        private String id;
//        private Set<statCnt> statCntSet;
//    }
//
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Setter
//    public static class statCnt {
//        private LocalDate localDate;
//        private int cnt = 0;
//    }
//
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Getter
//    @Setter
//    public static class EmpRequest {
//        private List<Service> services;
//        private String empId;
//    }
//
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Getter
//    @Setter
//    public static class EmpDashboardRes {
//        private EmpInfo employee;
//        private CounterInfo counter;
//        private int noOfWaiting;
//        private int noOfTransferred;
//        private String serviceName;
//        private String ongoingToken;
//    }
//
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class EmpInfo {
//        private String id;
//        private String username;
//        private Set<Role> roles;
//        private String firstname;
//        private String lastname;
//        private String middlename;
//        private boolean active;
//        private String loggedCounter;
//        private LocalDateTime loggedTime;
//    }
//
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class CounterInfo {
//        private String id;
//        private String name;
//        private String displayName;
//        private String description;
//        private String code;
//        private boolean active = true;
//        private Counter.Type type;
//        private boolean inUse = false;
//        private String ctrStatus;
//    }
//}
