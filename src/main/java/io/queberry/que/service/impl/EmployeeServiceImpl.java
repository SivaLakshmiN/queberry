package io.queberry.que.service.impl;

import io.queberry.que.config.TenantContext;
import io.queberry.que.dto.*;
import io.queberry.que.entity.*;
import io.queberry.que.enums.Status;
import io.queberry.que.exception.QueueException;
import io.queberry.que.repository.*;
import io.queberry.que.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PasswordManagementRepository passwordManagementRepository;
    private final AuditLogsRepository auditLogsRepository;
    private final BranchRepository branchRepository;
    private final RegionRepository regionRepository;
    private final ServiceRepository serviceRepository;
    private final CounterRepository counterRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionRepository sessionRepository;
//    @Autowired
//    private AssistanceRepository assistanceRepository;
//    @Autowired
//    private AppointmentRepository appointmentRepository;

@Override
public String resetPassword(PasswordResetDTO resetDTO, HttpServletRequest request) {
    List<Employee> employees = employeeRepository.findAllByUsername(resetDTO.getUsername());
    if (employees.isEmpty()) {
        return "User not found.";
    } else if (employees.size() > 1) {
        return "Multiple users found with the same username. Please contact the administrator.";
    }

    Employee employee = employees.get(0);
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    if (!encoder.matches(resetDTO.getOldPassword(), employee.getPassword())) {
        return "Old password is incorrect.";
    }

    if (resetDTO.getOldPassword().equals(resetDTO.getNewPassword())) {
        return "New password must be different from the old password.";
    }

    Optional<PasswordManagement> passwordManagementOpt =
            passwordManagementRepository.findFirstByUsernameIgnoreCaseOrderByIdDesc(resetDTO.getUsername());
    if (passwordManagementOpt.isPresent()) {
        PasswordManagement passwordManagement = passwordManagementOpt.get();
        List<String> lastPasswords = Arrays.asList(passwordManagement.getPasswords().split(","));

        for (String storedPassword : lastPasswords) {
            if (encoder.matches(resetDTO.getNewPassword(), storedPassword)) {
                return "New password must not match any of the last passwords.";
            }
        }
        passwordManagement.updatePasswordHistory(resetDTO.getNewPassword());
        passwordManagementRepository.save(passwordManagement);
    } else {
        return "Password history not found.";
    }

    employee.resetPassword(resetDTO.getOldPassword(), resetDTO.getNewPassword());
    employeeRepository.save(employee);
    AuditLogs logs = new AuditLogs();
    logs.setEntityName("User");
    logs.setEntityId(employee.getId());
    logs.setEntityField("Password");
    logs.setNewData("Password was reset");
    logs.setCreatedBy(resetDTO.getUsername());
    auditLogsRepository.save(logs);

    return "Password reset successfully.";
}

    @Override
    public int getActiveCount() {
        return employeeRepository.countAllByActiveTrue();
    }

    public void updateMasterUser(Employee e) {
        if (!TenantContext.getCurrentTenant().equals("queberry")) {
            TenantContext.setCurrentTenant("queberry");
            Employee employee = employeeRepository.findByUsername(e.getUsername());
            employee.setActive(e.isActive());
            employee.setPassword(e.getPassword());
            employeeRepository.save(employee);
        }
    }

    @Override
    public EmployeeRequest getEmployeeById(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return toDto(employee);
    }

    @Override
    public List<EmployeeRequest> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Employee deactivateEmployee(String id, HttpServletRequest request) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);

        if (optionalEmployee.isEmpty()) {
            throw new QueueException("User id doesn't exist!!!!", HttpStatus.NOT_FOUND);
        }

        Employee employee = optionalEmployee.get();
        employee.deActivate(); // assuming this sets status to "inactive"
        employee = employeeRepository.save(employee);

        AuditLogs auditLogs = new AuditLogs();
        auditLogs.setEntityName("User");
        auditLogs.setEntityId(employee.getId());
        auditLogs.setEntityField("Status");
        auditLogs.setOldData("active");
        auditLogs.setNewData("inactive");
        auditLogsRepository.save(auditLogs);

        updateMasterUser(employee);

        return employee;
    }

//    @Override
//    public Page<Employee> filterEmployeesByUsername(String username, Pageable pageable, HttpServletRequest request) {
//        Principal principal = request.getUserPrincipal();
//
//        if (principal == null) {
//            throw new IllegalStateException("User is not authenticated");
//        }
//
//        String loggedInUsername = principal.getName();
//        Employee loggedInEmployee = employeeRepository.findByUsername(loggedInUsername);
//
//        if (loggedInEmployee == null) {
//            throw new IllegalStateException("Logged-in employee not found");
//        }
//
//        Set<Role> roles = loggedInEmployee.getAuthorities();
//
//        boolean isAdmin = roles.contains(roleRepository.findByName("PRODUCT_ADMIN")) ||
//                roles.contains(roleRepository.findByName("ORG_ADMIN"));
//
//        if (isAdmin) {
//            return employeeRepository.findByUsernameContainingIgnoreCase(username, pageable);
//        } else {
//            Set<String> userBranches = loggedInEmployee.getBranches();
//            Set<Employee> filteredEmployees = employeeRepository.findByBranchesIn(userBranches).stream()
//                    .filter(emp -> emp.getUsername() != null &&
//                            emp.getUsername().toLowerCase().contains(username.toLowerCase()))
//                    .collect(Collectors.toSet());
//
//            return paginateSet(filteredEmployees, pageable);
//        }
//    }

    @Override
    public Page<EmployeeRequest> filterEmployeesByUsername(String username, Pageable pageable) {
        return employeeRepository.findByUsernameContainingIgnoreCase(username, pageable)
                .map(this::toDto);
    }

    private Page<Employee> paginateSet(Set<Employee> employees, Pageable pageable) {
        List<Employee> employeeList = new ArrayList<>(employees);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), employeeList.size());
        List<Employee> subList = employeeList.subList(start, end);
        return new PageImpl<>(subList, pageable, employeeList.size());
    }

    @Override
    public EmployeeRequest createEmployee(EmployeeData employeeData) {
        String createdBy = "system";

        Employee employee = new Employee();
        employee.setId(UUID.randomUUID().toString());
        employee.setCounter(employeeData.getCounter());
        employee.setPassword(new BCryptPasswordEncoder().encode(employeeData.getPassword()));

        Employee updated = setEmployeeInfo(employee, employeeData, createdBy);

        PasswordManagement pwdEntity = new PasswordManagement();
        pwdEntity.setId(UUID.randomUUID().toString());
        pwdEntity.setUsername(employeeData.getUsername());
        pwdEntity.setPasswords(new BCryptPasswordEncoder().encode(employeeData.getPassword()));

        passwordManagementRepository.save(pwdEntity);

        AuditLogs log = new AuditLogs();
        log.setEntityName("User");
        log.setEntityId(updated.getId());
        log.setNewData("New user created");
        log.setCreatedBy(createdBy);
        auditLogsRepository.save(log);

        return toDto(updated);
    }


    @Override
    public EmployeeRequest updateEmployee(String id, EmployeeData employeeData) {
        String updatedBy = "system";

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Employee updated = setEmployeeInfo(employee, employeeData, updatedBy);

        AuditLogs log = new AuditLogs();
        log.setEntityName("User");
        log.setEntityId(updated.getId());
        log.setNewData("User updated");
        log.setCreatedBy(updatedBy);
        auditLogsRepository.save(log);

        return toDto(updated);
    }

    private Employee setEmployeeInfo(Employee employee, EmployeeData data, String username) {
        employee.setUsername(data.getUsername());
        employee.setFirstname(data.getFirstname());
        employee.setMiddlename(data.getMiddlename());
        employee.setLastname(data.getLastname());
        employee.setActive(data.isActive());
        employee.setCallByNumber(data.isCallByNumber());
        employee.setWalkIn(data.isWalkIn());
        employee.setCallAll(data.isCallAll());
        employee.setCallNew(data.isCallNew());
        employee.setCallTransfer(data.isCallTransfer());
        employee.setShowServiceList(data.isShowServiceList());
        employee.setEnableAutoCall(data.isEnableAutoCall());
        employee.setForceAutoCall(data.isForceAutoCall());
        employee.setPark(data.isPark());
        employee.setTransferService(data.isTransferService());
        employee.setTransferCounter(data.isTransferCounter());
        employee.setTransferUser(data.isTransferUser());
        employee.setBreak_btn(data.isBreak_btn());
        employee.setCounter(data.getCounter());
        employee.setTenant(TenantContext.getCurrentTenant());

        employee.setRegion(data.getRegion());

        Set<Role> roleSet = data.getRoles().stream()
                .map(roleRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        employee.setRoles(roleSet);

        Set<String> branchSet = new HashSet<>(data.getBranches());
        employee.setBranches(branchSet);
        Set<String> serviceSet = new HashSet<>(data.getServices());
        employee.setServices(serviceSet);
        employee.setSecond(data.getSecond() != null ? data.getSecond() : new TreeSet<>());
        employee.setThird(data.getThird() != null ? data.getThird() : new TreeSet<>());
        employee.setFourth(data.getFourth() != null ? data.getFourth() : new TreeSet<>());
        return employeeRepository.save(employee);
    }

//private EmployeeRequest toDto(Employee employee) {
//    List<Branch> branchEntities = employee.getBranches().stream()
//            .map(branchRepository::findById)
//            .filter(Optional::isPresent)
//            .map(Optional::get)
//            .collect(Collectors.toList());
//
//    List<BranchDTO> branchDTOs = branchEntities.stream()
//            .map(branch -> new BranchDTO())
//            .collect(Collectors.toList());
//
//    RegionDTO regionDTO = regionRepository.findById(employee.getRegion())
//            .map(region -> new RegionDTO(region.getId(), region.getName()))
//            .orElse(new RegionDTO(employee.getRegion(), "null"));
//
//    List<ServiceDTO> serviceDTOs = employee.getServices().stream()
//            .map(serviceId -> serviceRepository.findById(serviceId))
//            .filter(Optional::isPresent)
//            .map(Optional::get)
//            .map(service -> new ServiceDTO())
//            .collect(Collectors.toList());
//
//    return new EmployeeRequest(employee, regionDTO, branchDTOs, serviceDTOs);
//}


    private EmployeeRequest toDto(Employee employee) {
        List<BranchesDTO> branchDTOs = employee.getBranches().stream()
                .map(branchId -> branchRepository.findById(branchId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(branch -> new BranchesDTO(branch.getId(), branch.getName(),branch.getBranchKey()))
                .collect(Collectors.toList());

        RegionDTO regionDTO = regionRepository.findById(employee.getRegion())
                .map(region -> new RegionDTO(region.getId(), region.getName()))
                .orElse(new RegionDTO(employee.getRegion(), "null"));

        List<ServicesDTO> serviceDTOs = employee.getServices().stream()
                .map(serviceId -> serviceRepository.findById(serviceId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(service -> new ServicesDTO(service.getId(), service.getName()))
                .collect(Collectors.toList());

        return new EmployeeRequest(employee, regionDTO, branchDTOs, serviceDTOs);
    }

    @Override
    public Employee resetUserPassword(String username, String newPassword) {
        Employee employee = employeeRepository.findByUsername(username);
        if (employee == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        employee.resetAdminPassword(newPassword);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee unlockEmployee(String id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (!optionalEmployee.isPresent()) {
            return null;
        }

        Employee employee = optionalEmployee.get();
        employee.setLocked(false);
        employee = employeeRepository.save(employee);

        AuditLogs auditLogs = new AuditLogs();
        auditLogs.setEntityName("User");
        auditLogs.setEntityId(employee.getId());
        auditLogs.setEntityField("Status");
        auditLogs.setOldData("locked");
        auditLogs.setNewData("unlocked");
        auditLogs.setCreatedBy("admin"); // or fetch from session/context
        auditLogsRepository.save(auditLogs);

        updateMasterUser(employee);

        return employee;
    }
    @Override
    public Employee assignCounter(String employeeId, String counter) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null) throw new RuntimeException("User id doesn't exist!");

        if (!employee.getAuthorities().contains(roleRepository.findByName("COUNTER_AGENT"))) {
            throw new RuntimeException("User is not a Counter Agent to Assign a Counter");
        }

        AuditLogs auditLogs = new AuditLogs();
        auditLogs.setEntityName("User");
        auditLogs.setEntityId(employee.getId());
        auditLogs.setEntityField("Counter");
        auditLogs.setOldData(employee.getCounter() != null ? employee.getCounter() : null);
        auditLogs.setNewData(counter);
        auditLogs.setCreatedBy("admin"); // Replace as needed
        auditLogsRepository.save(auditLogs);

        Counter counters = counterRepository.findCounterById(counter).orElse(null);
        employee.setCounter(counter);
        return employeeRepository.save(employee);
    }

    @Override
    public String forgotPassword(ForgotPasswordDTO forgotDTO) {
        Employee employee = employeeRepository.findByUsername(forgotDTO.getUsername());
        if (employee == null) {
            return "User not found.";
        }

        String newPassword = forgotDTO.getNewPassword();
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return "New password cannot be empty.";
        }

        if (passwordEncoder.matches(newPassword, employee.getPassword())) {
            return "New password must be different from the current password.";
        }

        employee.setPassword(passwordEncoder.encode(newPassword));
        employeeRepository.save(employee);

        return "Password updated via forgot password process.";
    }

    @Override
    public Employee updateUsername(String employeeId, String newUsername) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null) throw new RuntimeException("User id doesn't exist!");

        employee.setUsername(newUsername);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee detachCounter(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null) throw new RuntimeException("Employee id doesn't exist!");

        employee.setCounter(null);
        return employeeRepository.save(employee);
    }

    @Override
    public String deleteEmployee(String employeeId, String loggedInUsername) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null) return "User id doesn't exist!";

        if (employee.getUsername().equals(loggedInUsername)) {
            return "Cannot delete logged in Employee!!";
        }

        employee.setCounter(null);
        employeeRepository.save(employee);
        employeeRepository.delete(employee);
        return "Employee deleted successfully.";
    }
    @Override
    public List<Employee> getActiveCounterAgents(String branchKey) {
        Branch branch = branchRepository.findByBranchKey(branchKey);
        return employeeRepository.findByActiveTrueAndBranchesAndRolesContaining(
                branch, roleRepository.findByName("COUNTER_AGENT"));
    }

    @Override
    public EmpDashboardDtls getEmployeeDashboard(EmpDashboardRequest services) {
        EmpDashboardDtls empDashboardDtls = new EmpDashboardDtls();

        try {
            Employee emp = employeeRepository.findById(services.getEmployeeId()).orElseThrow();

            LocalDate sdate = LocalDate.parse(services.getStartDate(),
                    DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            LocalDate edate = LocalDate.parse(services.getEndDate(),
                    DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            Set<String> serviceIds = services.getServices().stream().map(ServiceDTO::getId).collect(Collectors.toSet());

            empDashboardDtls.setEmployeeId(emp.getId());
            empDashboardDtls.setEmployeeName(emp.getUsername());

            Set<Session> empSessions;
            Map<String, Long> inQueData = new HashMap<>();
            Long totalServeTime = 0L;
            AtomicReference<Long> totalWaitTime = new AtomicReference<>(0L);

            if (!serviceIds.isEmpty()) {
                empSessions = sessionRepository.findByEmployeeAndServiceIdInAndCreatedAtBetween(
                        emp.getId(), serviceIds, LocalDateTime.of(sdate, LocalTime.MIN), LocalDateTime.of(edate, LocalTime.MAX)
                );
                inQueData = getWaitTimePerService(serviceIds, sdate, edate);
            } else {
                empSessions = sessionRepository.findByEmployeeAndCreatedAtBetween(
                        emp.getId(), LocalDateTime.of(sdate, LocalTime.MIN), LocalDateTime.of(edate, LocalTime.MAX)
                );
            }

            if (!empSessions.isEmpty()) {
                totalServeTime = empSessions.stream().map(Session::getServeTime).reduce(Long::sum).orElse(0L);

                Map<Status, Long> statusCount = empSessions.stream()
                        .collect(Collectors.groupingBy(Session::getStatus, Collectors.counting()));

                empDashboardDtls.setTotalParked(statusCount.getOrDefault(Status.HOLD, 0L).intValue());
                empDashboardDtls.setTotalNoShow(statusCount.getOrDefault(Status.NO_SHOW, 0L).intValue());
                empDashboardDtls.setTotalCompleted(statusCount.getOrDefault(Status.COMPLETED, 0L).intValue());
                empDashboardDtls.setTotalTransferred((int) statusCount.entrySet().stream()
                        .filter(entry -> Arrays.asList(
                                Status.TRANSFERRED_TO_COUNTER,
                                Status.TRANSFERRED_TO_SERVICE,
                                Status.TRANSFERRED_TO_USER
                        ).contains(entry.getKey()))
                        .mapToLong(Map.Entry::getValue).sum());
            }

//            Set<Assistance> assistances = (!serviceIds.isEmpty())
//                    ? assistanceRepository.findByCreatedAtBetweenAndSessionsEmployeeAndSessionsServiceIdIn(
//                    LocalDateTime.of(sdate, LocalTime.MIN), LocalDateTime.of(edate, LocalTime.MAX),
//                    emp.getId(), serviceIds
//            )
//                    : assistanceRepository.findByCreatedAtBetweenAndSessionsEmployeeAndOptionalBranch(
//                    LocalDateTime.of(sdate, LocalTime.MIN), LocalDateTime.of(edate, LocalTime.MAX),
//                    emp.getId(), null
//            );
//
//            assistances = assistances.stream()
//                    .filter(a -> a.getStatus() != Status.EXPIRED)
//                    .collect(Collectors.toSet());

//            if (!assistances.isEmpty()) {
//                for (Assistance assistance : assistances) {
//                    AtomicReference<LocalDateTime> prevComplete = new AtomicReference<>(null);
//                    Stream<Session> sortedSessions = assistance.getSessions().stream()
//                            .sorted(Comparator.comparing(Session::getServingStart));
//
//                    sortedSessions.forEach(session -> {
//                        if (prevComplete.get() == null) {
//                            if (session.getEmployee().equals(emp.getId())) {
//                                totalWaitTime.set(totalWaitTime.get() + Duration.between(assistance.getCreatedAt(), session.getServingStart()).getSeconds());
//                            }
//                        } else {
//                            if (session.getEmployee().equals(emp.getId())) {
//                                totalWaitTime.set(totalWaitTime.get() + Duration.between(prevComplete.get(), session.getServingStart()).getSeconds());
//                            }
//                        }
//                        prevComplete.set(session.getCompletedAt());
//                    });
//                }
//                empDashboardDtls.setTotalTokens(assistances.size());
//            }

            empDashboardDtls.setAvgWaitTime("00:00:00");
            empDashboardDtls.setAvgServeTime("00:00:00");

            if (empDashboardDtls.getTotalTokens() != 0) {
                int avgServeTime = round(totalServeTime / empDashboardDtls.getTotalTokens());
                int avgWaitTime = round(totalWaitTime.get() / empDashboardDtls.getTotalTokens());
                empDashboardDtls.setAvgWaitTime(secConvert(avgWaitTime));
                empDashboardDtls.setAvgServeTime(secConvert(avgServeTime));
            }

            empDashboardDtls.InQueWaitTime = secConvert(inQueData.getOrDefault("InQueWaitTime", 0L));
            empDashboardDtls.InQueAvgWaitTime = secConvert(inQueData.getOrDefault("InQueAvgWaitTime", 0L));
        } catch (Exception e) {
            log.error("Error generating dashboard: ", e);
        }

        return empDashboardDtls;
    }

    private String secConvert(long seconds) {
        return String.format("%02d:%02d:%02d",  seconds / 3600, (seconds % 3600) / 60, seconds % 60);
    }

    private int round(long value) {
        return (int) Math.round(value);
    }

    private Map<String, Long> getWaitTimePerService(Set<String> serviceIds, LocalDate sdate, LocalDate edate) {
        Map<String, Long> result = new HashMap<>();
        result.put("InQueWaitTime", 120L);
        result.put("InQueAvgWaitTime", 30L);
        return result;
    }

//    @Override
//    public Page<Appointment> getAppointmentList(EmpDashboardRequest services, Pageable pageable) {
//        Set<com.example.QueApplication.Entity.Service> swList = new HashSet<>();
//        Set<Appointment.State> stateList = Set.of(Appointment.State.CONFIRMED, Appointment.State.CHECKEDIN);
//
//        if (services.getServices() != null && !services.getServices().isEmpty()) {
//            services.getServices().forEach(serviceDto -> {
//                serviceRepository.findById(serviceDto.getId()).ifPresent(swList::add);
//            });
//        }
//
//        return appointmentRepository.findByServiceInAndDateAndStateIn(
//                swList, LocalDate.now(), stateList, pageable);
//    }
@Override
public Employee activateEmployee(String id, String performedBy) {
    Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Employee with ID " + id + " not found"));

    employee.activate();
    Employee updatedEmployee = employeeRepository.save(employee);

    AuditLogs log = new AuditLogs();
    log.setEntityName("User");
    log.setEntityId(updatedEmployee.getId());
    log.setEntityField("Status");
    log.setOldData("inactive");
    log.setNewData("active");
    log.setCreatedBy(performedBy != null ? performedBy : "system");
    auditLogsRepository.save(log);

    return updatedEmployee;
}

}
