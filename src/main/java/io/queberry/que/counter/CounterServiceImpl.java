package io.queberry.que.counter;

import io.queberry.que.assistance.AssistanceRepository;
import io.queberry.que.auditLogs.AuditLogsRepository;
import io.queberry.que.branch.BranchRepository;
import io.queberry.que.employee.EmployeeRepository;
import io.queberry.que.dto.ServiceList;
import io.queberry.que.auditLogs.AuditLogs;
import io.queberry.que.branch.Branch;
import io.queberry.que.employee.Employee;
import io.queberry.que.exception.DataNotFoundException;
import io.queberry.que.service.ServiceRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@org.springframework.stereotype.Service
public class CounterServiceImpl implements CounterService {
    private final CounterRepository counterRepository;
    private final BranchRepository branchRepository;
    private final ServiceRepository serviceRepository;
    private final EmployeeRepository employeeRepository;
    //    private final WebSocketOperations webSocketOperations;
    private final AssistanceRepository assistanceRepository;
    private final AuditLogsRepository auditLogsRepository;
    @Autowired
    public CounterServiceImpl(CounterRepository counterRepository, BranchRepository branchRepository, ServiceRepository serviceRepository, EmployeeRepository employeeRepository,  AssistanceRepository assistanceRepository, AuditLogsRepository auditLogsRepository) {
        this.counterRepository = counterRepository;
        this.branchRepository = branchRepository;
        this.serviceRepository = serviceRepository;
        this.employeeRepository = employeeRepository;
//        this.webSocketOperations = webSocketOperations;
        this.assistanceRepository = assistanceRepository;
        this.auditLogsRepository = auditLogsRepository;
    }
    @Override
    public Set<Counter> activeFindAll(Branch branch) {
        return counterRepository.findByBranchAndActiveTrue(branch, Sort.by(Sort.Order.asc("name")));
    }
    @Override
    public Counter activate(Counter counter) {
        if (counter == null) {
            throw new IllegalArgumentException("Counter not found");
        }
        counter.activate();
        return counterRepository.save(counter);
    }
    @Override
    public Counter deactivate(String counterId) {
        return counterRepository.findCounterById(counterId)
                .map(counter -> {
                    counter.deactivate();
                    return counterRepository.save(counter);
                })
                .orElseThrow(() -> new NoSuchElementException("Counter not found with ID: " + counterId));
    }
    @Override
    public Counter save(CounterResources resource) {
        Counter counter = new Counter();
        counter.setActive(resource.isActive());
        counter.setCode(resource.getCode());
        counter.setDescription(resource.getDescription());
        counter.setName(resource.getName());
        counter.setDisplayName(resource.getDisplayName());
        counter.setBranch(resource.getBranchKey());
        counter.setFirst(new HashSet<>(serviceRepository.findByIdIn(resource.getFirst())));
        counter.setSecond(new HashSet<>(serviceRepository.findByIdIn(resource.getSecond())));
        counter.setThird(new HashSet<>(serviceRepository.findByIdIn(resource.getThird())));
        counter.setFourth(new HashSet<>(serviceRepository.findByIdIn(resource.getFourth())));
        if (resource.getType() != null) {
            counter.setType(Counter.Type.valueOf(resource.getType()));
        }
        if (resource.getColorCode() != null) {
            counter.setColorCode(resource.getColorCode());
        }
        if (resource.getPanelNumber() != null) {
            counter.setPanelNumber(resource.getPanelNumber());
        }
        if (resource.getPresentation() != null) {
            counter.setPresentation(resource.getPresentation());
        }
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Counter>> errors = validator.validate(counter);
        if (!errors.isEmpty()) {
            StringBuilder validationErrors = new StringBuilder();
            for (ConstraintViolation<Counter> error : errors) {
                validationErrors.append(error.getMessageTemplate()).append("; ");
            }
            throw new IllegalArgumentException("Validation failed: " + validationErrors);
        }
        return counterRepository.save(counter);
    }
    @Override
    public Counter editCounter(String counterId, CounterResources resource) {
        Optional<Counter> optionalCounter = counterRepository.findCounterById(counterId);
        if (!optionalCounter.isPresent()) {
            throw new NoSuchElementException("Counter ID doesn't exist");
        }
        Counter counter = optionalCounter.get();
        counter.setActive(resource.isActive());
        counter.setDescription(resource.getDescription());
        counter.setName(resource.getName());
        counter.setDisplayName(resource.getDisplayName());
        counter.setBranch(resource.getBranchKey());
        counter.setFirst(new HashSet<>(serviceRepository.findByIdIn(resource.getFirst())));
        counter.setSecond(new HashSet<>(serviceRepository.findByIdIn(resource.getSecond())));
        counter.setThird(new HashSet<>(serviceRepository.findByIdIn(resource.getThird())));
        counter.setFourth(new HashSet<>(serviceRepository.findByIdIn(resource.getFourth())));
        if (resource.getType() != null) {
            counter.setType(Counter.Type.valueOf(resource.getType()));
        }
        if (resource.getColorCode() != null) {
            counter.setColorCode(resource.getColorCode());
        }
        if (resource.getPanelNumber() != null) {
            counter.setPanelNumber(resource.getPanelNumber());
        }
        if (resource.getPresentation() != null) {
            counter.setPresentation(resource.getPresentation());
        }
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Counter>> errors = validator.validate(counter);
        if (!errors.isEmpty()) {
            StringBuilder validationErrors = new StringBuilder();
            for (ConstraintViolation<Counter> error : errors) {
                validationErrors.append(error.getMessageTemplate()).append("; ");
            }
            throw new IllegalArgumentException(validationErrors.toString());
        }
        return counterRepository.save(counter);
    }
    @Override
    public Counter addServices(Counter counter, ServiceList serviceList) {
        if (counter == null) {
            throw new NoSuchElementException("Counter does not exist");
        }
        Set<String> firstLevel = new HashSet<>(serviceRepository.findByIdIn(serviceList.getFirst()));
        counter.setFirst(firstLevel);
        if (serviceList.getSecond() != null) {
            Set<String> secondLevel = new HashSet<>(serviceRepository.findByIdIn(serviceList.getSecond()));
            counter.setSecond(secondLevel);
        }
        if (serviceList.getThird() != null) {
            Set<String> thirdLevel = new HashSet<>(serviceRepository.findByIdIn(serviceList.getThird()));
            counter.setThird(thirdLevel);
        }
        if (serviceList.getFourth() != null) {
            Set<String> fourthLevel = new HashSet<>(serviceRepository.findByIdIn(serviceList.getFourth()));
            counter.setFourth(fourthLevel);
        }
        return counterRepository.save(counter);
    }
    @Override
    public Set<Counter> listUnassignedCounters(String branchId) {
        Set<String> assignedCounters = new HashSet<>();
        Set<String> branches = new HashSet<>();
        branches.add(branchId);

        Set<Employee> employees = employeeRepository.findByBranchesIn(branches);
        for (Employee employee : employees) {
            if (employee != null && employee.getCounter() != null) {
                assignedCounters.add(employee.getCounter());
                assignedCounters.add(String.valueOf(employee.getCounter()));
            }
        }

        Set<Counter> counters;
        if (!assignedCounters.isEmpty()) {
            counters = counterRepository.findAllByIdNotInAndBranchAndActiveIsTrueAndInUseFalse(
                    assignedCounters,
                    branchId,
                    Sort.by(Sort.Order.asc("name"))
            );
        } else {
            counters = counterRepository.findAllByActiveIsTrueAndBranchAndInUseFalse(
                    branchId,
                    Sort.by(Sort.Order.asc("name"))
            );
        }
        return counters;
    }
    @Override
    public Page<Counter> filterCountersByCode(String branchId, String codeFragment, Pageable pageable) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new DataNotFoundException("Branch not found with ID: " + branchId));

        return counterRepository.findByBranchAndCodeContainingIgnoreCase(branch, codeFragment, pageable);
    }

    @Override
    public Counter inUse(String counterId) {
        if (counterId == null || counterId.trim().isEmpty()) {
            throw new DataNotFoundException("Counter ID is missing");
        }
        Counter counter = counterRepository.findCounterById(counterId)
                .orElseThrow(() -> new DataNotFoundException("Counter not found with ID: " + counterId));
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
//        boolean isBusy = assistanceRepository
//                .findByStatusAndSessionsCounterAndCreatedAtBetween(
//                        Status.ATTENDING, counter.getId(), start, end, Pageable.unpaged()
//                ).hasContent();
//
//        if (isBusy) {
//            throw new IllegalStateException("Counter is currently serving a token");
//        }
        counter.setInUse(false);
        AuditLogs log = new AuditLogs();
        log.setEntityName("Counter");
        log.setEntityId(counter.getId());
        log.setEntityField("inUse");
        log.setOldData("occupied");
        log.setNewData("free");
        auditLogsRepository.save(log);
//        employeeRepository.findByLoggedCounter(counter.getId()).ifPresent(emp ->
//                webSocketOperations.send("/notifications/employee/" + emp.getUsername(), "logout")
//        );

        return counterRepository.save(counter);
    }
    @Override
    public Counter disableCounter(HttpServletRequest request,String counterId) {
        String token = request.getHeader("Authorization").substring(7);

//        if (jwtTokenUtil.isTokenExpired(token)) {
//            throw new RuntimeException("JWT token expired!");
//        }
//
//        String username = jwtTokenUtil.getUsernameFromToken(token);
//
        Counter counter = counterRepository.findById(counterId)
                .orElseThrow(() -> new DataNotFoundException("Counter not found with ID: " + counterId));

        counter.setActive(false);

        AuditLogs auditLogs = new AuditLogs();
        auditLogs.setEntityName("Counter");
        auditLogs.setEntityId(counter.getId());
        auditLogs.setEntityField("Status");
        auditLogs.setOldData("inactive");
        auditLogs.setNewData("active");
//        auditLogs.setCreatedBy(username);

        auditLogsRepository.save(auditLogs);
        return counterRepository.save(counter);
    }
    @Override
    public Counter enableCounter(HttpServletRequest request, String counterId) {
        String token = request.getHeader("Authorization").substring(7);

//        if (jwtTokenUtil.isTokenExpired(token)) {
//            throw new RuntimeException("JWT token expired!");
//        }
//
//        String username = jwtTokenUtil.getUsernameFromToken(token);
//
        Counter counter = counterRepository.findById(counterId)
                .orElseThrow(() -> new DataNotFoundException("Counter not found with ID: " + counterId));

        counter.setActive(true);

        AuditLogs auditLogs = new AuditLogs();
        auditLogs.setEntityName("Counter");
        auditLogs.setEntityId(counter.getId());
        auditLogs.setEntityField("Status");
        auditLogs.setOldData("inactive");
        auditLogs.setNewData("active");
//        auditLogs.setCreatedBy(username);

        auditLogsRepository.save(auditLogs);
        return counterRepository.save(counter);
    }
    @Override
    public Page<Counter> getCounters(Pageable pageable) {
        return counterRepository.findAll(pageable);
    }
    @Transactional
    @Override
    public Counter exitCounter(String counterId, String empId) {
        Counter counter = counterRepository.findCounterById(counterId)
                .orElseThrow(() -> new DataNotFoundException("Counter not found: " + counterId));

        counter.setInUse(false);
        counter = counterRepository.save(counter);

        // Unassign employee from counter
        employeeRepository.findEmployeeById(empId).ifPresent(emp -> {
            emp.setLoggedCounter(null);
            emp.setLoggedTime(null);
            employeeRepository.save(emp);
        });

//        // Remove queue-counter mapping
//        queueCounterRepository.findByCounterId(counterId).stream().findFirst().ifPresent(queueCounterRepository::delete);
//
//        // Notify front-end
//        messagingTemplate.convertAndSend(
//                "/notifications/" + counter.getBranch().getBranchKey() + "/counterDisplayMessage",
//                new BreakController.CounterStatusWrapper(counter, "CLOSED")
//        );

        // Set counter status
//        counterStatusService.setStatus(counter.getId(), "CLOSED");

        return counter;
    }
}
