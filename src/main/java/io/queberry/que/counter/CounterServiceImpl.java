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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    public CounterServiceImpl(CounterRepository counterRepository, BranchRepository branchRepository, ServiceRepository serviceRepository, EmployeeRepository employeeRepository, AssistanceRepository assistanceRepository, AuditLogsRepository auditLogsRepository) {
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
        log.info("Fetching active counters for branch: {}", branch.getId());
        return counterRepository.findByBranchAndActiveTrue(branch, Sort.by(Sort.Order.asc("name")));
    }

    @Override
    public Counter activate(Counter counter) {
        log.info("Activating counter: {}", counter != null ? counter.getId() : "null");
        if (counter == null) {
            throw new IllegalArgumentException("Counter not found");
        }
        counter.activate();
        return counterRepository.save(counter);
    }

    @Override
    public Counter deactivate(String counterId) {
        log.info("Deactivating counter with ID: {}", counterId);
        return counterRepository.findCounterById(counterId)
                .map(counter -> {
                    counter.deactivate();
                    return counterRepository.save(counter);
                })
                .orElseThrow(() -> new NoSuchElementException("Counter not found with ID: " + counterId));
    }

    @Override
    public Counter save(CounterResources resource) {
        log.info("Saving new counter: {}", resource.getCode());
        Counter counter = new Counter();
        // [populate counter fields...]
        // [validation...]
        return counterRepository.save(counter);
    }

    @Override
    public Counter editCounter(String counterId, CounterResources resource) {
        log.info("Editing counter with ID: {}", counterId);
        Optional<Counter> optionalCounter = counterRepository.findCounterById(counterId);
        if (!optionalCounter.isPresent()) {
            log.warn("Counter not found: {}", counterId);
            throw new NoSuchElementException("Counter ID doesn't exist");
        }
        // [update fields...]
        return counterRepository.save(optionalCounter.get());
    }

    @Override
    public Counter addServices(Counter counter, ServiceList serviceList) {
        log.info("Adding services to counter: {}", counter != null ? counter.getId() : "null");
        if (counter == null) {
            throw new NoSuchElementException("Counter does not exist");
        }
        // [assign services...]
        return counterRepository.save(counter);
    }

    @Override
    public Set<Counter> listUnassignedCounters(String branchId) {
        log.info("Listing unassigned counters for branch: {}", branchId);
        Set<String> assignedCounters = new HashSet<>();
        // [collect assigned counters...]
        return assignedCounters.isEmpty() ?
                counterRepository.findAllByActiveIsTrueAndBranchAndInUseFalse(branchId, Sort.by(Sort.Order.asc("name"))) :
                counterRepository.findAllByIdNotInAndBranchAndActiveIsTrueAndInUseFalse(assignedCounters, branchId, Sort.by(Sort.Order.asc("name")));
    }

    @Override
    public Page<Counter> filterCountersByCode(String branchId, String codeFragment, Pageable pageable) {
        log.info("Filtering counters for branch {} by code containing '{}'", branchId, codeFragment);
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new DataNotFoundException("Branch not found with ID: " + branchId));
        return counterRepository.findByBranchAndCodeContainingIgnoreCase(branch, codeFragment, pageable);
    }

    @Override
    public Counter inUse(String counterId) {
        log.info("Marking counter as free: {}", counterId);
        if (counterId == null || counterId.trim().isEmpty()) {
            throw new DataNotFoundException("Counter ID is missing");
        }
        Counter counter = counterRepository.findCounterById(counterId)
                .orElseThrow(() -> new DataNotFoundException("Counter not found with ID: " + counterId));
        counter.setInUse(false);

        AuditLogs logEntry = new AuditLogs();
        logEntry.setEntityName("Counter");
        logEntry.setEntityId(counter.getId());
        logEntry.setEntityField("inUse");
        logEntry.setOldData("occupied");
        logEntry.setNewData("free");
        auditLogsRepository.save(logEntry);

        return counterRepository.save(counter);
    }

    @Override
    public Counter disableCounter(HttpServletRequest request, String counterId) {
        log.info("Disabling counter: {}", counterId);
        String token = request.getHeader("Authorization").substring(7);
        // [JWT validation optional]
        Counter counter = counterRepository.findById(counterId)
                .orElseThrow(() -> new DataNotFoundException("Counter not found with ID: " + counterId));
        counter.setActive(false);

        AuditLogs auditLogs = new AuditLogs();
        auditLogs.setEntityName("Counter");
        auditLogs.setEntityId(counter.getId());
        auditLogs.setEntityField("Status");
        auditLogs.setOldData("inactive");
        auditLogs.setNewData("active");
        auditLogsRepository.save(auditLogs);

        return counterRepository.save(counter);
    }

    @Override
    public Counter enableCounter(HttpServletRequest request, String counterId) {
        log.info("Enabling counter: {}", counterId);
        String token = request.getHeader("Authorization").substring(7);
        // [JWT validation optional]
        Counter counter = counterRepository.findById(counterId)
                .orElseThrow(() -> new DataNotFoundException("Counter not found with ID: " + counterId));
        counter.setActive(true);

        AuditLogs auditLogs = new AuditLogs();
        auditLogs.setEntityName("Counter");
        auditLogs.setEntityId(counter.getId());
        auditLogs.setEntityField("Status");
        auditLogs.setOldData("inactive");
        auditLogs.setNewData("active");
        auditLogsRepository.save(auditLogs);

        return counterRepository.save(counter);
    }

    @Override
    public Page<Counter> getCounters(Pageable pageable) {
        log.info("Fetching all counters with pagination");
        return counterRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Counter exitCounter(String counterId, String empId) {
        log.info("Exiting counter: {} for employee: {}", counterId, empId);
        Counter counter = counterRepository.findCounterById(counterId)
                .orElseThrow(() -> new DataNotFoundException("Counter not found: " + counterId));
        counter.setInUse(false);
        counter = counterRepository.save(counter);

        employeeRepository.findEmployeeById(empId).ifPresent(emp -> {
            emp.setLoggedCounter(null);
            emp.setLoggedTime(null);
            employeeRepository.save(emp);
        });

        return counter;
    }
}