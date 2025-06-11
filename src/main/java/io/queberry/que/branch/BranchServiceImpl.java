package io.queberry.que.branch;

import io.queberry.que.assistance.Assistance;
import io.queberry.que.assistance.AssistanceRepository;
import io.queberry.que.counter.Counter;
import io.queberry.que.counter.CounterRepository;
import io.queberry.que.employee.Employee;
import io.queberry.que.employee.EmployeeRepository;
import io.queberry.que.role.Role;
import io.queberry.que.serviceGroup.ServiceGroup;
import io.queberry.que.serviceGroup.ServiceGroupDTO;
import io.queberry.que.serviceGroup.ServiceGroupRepository;
import io.queberry.que.serviceGroup.ServiceGroupRequest;
import io.queberry.que.dto.Capacity;
import io.queberry.que.enums.Status;
import io.queberry.que.exception.DataNotFoundException;
import io.queberry.que.mapper.BranchMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;
    private final ServiceGroupRepository serviceGroupRepository;
    private final CounterRepository counterRepository;
    private final AssistanceRepository assistanceRepository;
    private final EmployeeRepository employeeRepository;

    public BranchServiceImpl(BranchRepository branchRepository, BranchMapper branchMapper, ServiceGroupRepository serviceGroupRepository, CounterRepository counterRepository, AssistanceRepository assistanceRepository, EmployeeRepository employeeRepository) {
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
        this.serviceGroupRepository = serviceGroupRepository;
        this.counterRepository = counterRepository;
        this.assistanceRepository = assistanceRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Set<BranchDTO> getActiveBranches() {
        log.info("Fetching all active branches");
        Set<Branch> branches = branchRepository.findByActiveTrue();
        return branches.stream()
                .map(branchMapper::entityToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Set<BranchDTO> getActiveWebPrinterBranches() {
        log.info("Fetching all active web printer branches");
        Set<Branch> branches = branchRepository.findByActiveTrue();
        return branches.stream()
                .map(branchMapper::entityToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public String getActiveBranchCount() {
        log.info("Getting count of all active branches");
        return branchRepository.countAllByActiveTrue();
    }

    @Override
    public List<BranchDTO> getAllBranches() {
        log.info("Fetching all branches (DTOs)");
        return branchRepository.findAllBranchDTOs();
    }

    @Override
    public BranchDTO getBranchById(String id) throws DataNotFoundException {
        log.info("Fetching branch by ID: {}", id);
        Optional<Branch> optional = branchRepository.findById(id);
        if (optional.isPresent()) {
            return branchMapper.entityToDto(optional.get());
        }
        log.warn("Branch not found for ID: {}", id);
        throw new DataNotFoundException("not found");
    }

    @Override
    public Branch createBranch(Branch branch) {
        log.info("Creating branch: {}", branch.getName());
        return branchRepository.save(branch);
    }

    @Override
    public Branch updateBranch(String id, BranchRequest branchInfo) {
        log.info("Updating branch with ID: {}", id);
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Branch not found for ID: {}", id);
                    return new NoSuchElementException("Branch not found");
                });
        branch.setName(branchInfo.getName());
        branch.setBusinessStart(branchInfo.getBusinessStart());
        branch.setBusinessEnd(branchInfo.getBusinessEnd());
        branch.setRegion(branchInfo.getRegion());
        branch.setGlobalServices(branchInfo.isGlobalServices());
        branch.setComPort(branchInfo.getComPort());
        return branch;
    }

    @Override
    public boolean deleteBranch(String id) {
        log.info("Deleting branch with ID: {}", id);
        if (branchRepository.existsById(id)) {
            branchRepository.deleteById(id);
            return true;
        }
        log.warn("Attempted to delete non-existent branch ID: {}", id);
        return false;
    }

    @Override
    public Page<Branch> getBranchesByRegion(String regionId, Pageable pageable) {
        log.info("Fetching branches by region ID: {}", regionId);
        return branchRepository.findByRegion(regionId, pageable);
    }

    @Override
    public Branch activateBranch(String branchId) {
        log.info("Activating branch ID: {}", branchId);
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> {
                    log.warn("Branch not found for activation: {}", branchId);
                    return new DataNotFoundException("Branch not found with id " + branchId);
                });
        branch.setActive(true);
        branch = branchRepository.save(branch);
        return branch;
    }

    @Override
    public Branch deActivateBranch(String branchId) {
        log.info("Deactivating branch ID: {}", branchId);
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> {
                    log.warn("Branch not found for deactivation: {}", branchId);
                    return new DataNotFoundException("Branch not found with id " + branchId);
                });
        branch.setActive(false);
        branch = branchRepository.save(branch);

        Set<String> branches = new HashSet<>();
        // branches.add(branch); // commented out

        log.info("Removing branch [{}] from employees", branch.getName());
        Set<Employee> employees = employeeRepository.findByBranchesIn(branches);
        for (Employee employee : employees) {
            Set<String> employeeBranches = (Set<String>) employee; // this line seems incorrect
            employeeBranches.remove(branch);
            employeeRepository.save(employee);
        }

        return branch;
    }

    @Override
    public Page<BranchDTO> filterBranchesByName(HttpServletRequest request, String region, String brName, Pageable pageable) {
        String username = request.getUserPrincipal().getName();
        log.info("Filtering branches by name: '{}' in region: '{}' for user: '{}'", brName, region, username);

        Optional<Employee> employeeOpt = employeeRepository.findEmployeeByUsername(username);
        if (employeeOpt.isEmpty()) {
            log.warn("No employee found for username: {}", username);
            return Page.empty(pageable);
        }

        // Actual implementation is commented out and returning null.
        log.warn("filterBranchesByName is not fully implemented, returning null");
        return null;
    }

    @Override
    public Branch assignServiceGroup(String branchId, ServiceGroupRequest request) {
        log.info("Assigning service group to branch ID: {}", branchId);
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> {
                    log.warn("Branch not found with ID: {}", branchId);
                    return new DataNotFoundException("Branch not found");
                });

        ServiceGroup serviceGroup = new ServiceGroup();
        serviceGroup.setName(request.getName());
        serviceGroup.setDisplayName(request.getDisplayName());
        serviceGroup.setNames(request.getNames());
        serviceGroup = serviceGroupRepository.save(serviceGroup);

        Set<String> serviceGroups = branch.getServiceGroup();
        if (serviceGroups == null) {
            serviceGroups = new HashSet<>();
        }

        serviceGroups.add(serviceGroup.getName());
        branch.setServiceGroup(serviceGroups);

        return branchRepository.save(branch);
    }

    @Override
    public Set<ServiceGroupDTO> getServiceGroupsByBranchKey(String branchKey) {
        log.info("Fetching service groups by branch key: {}", branchKey);
        Branch branch = branchRepository.findByBranchKey(branchKey);
        if (branch == null) {
            log.warn("Branch not found with key: {}", branchKey);
            throw new RuntimeException("Branch not found with key: " + branchKey);
        }
        return branch.getServiceGroup().stream()
                .map(ServiceGroupDTO::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Capacity getBranchCapacity(String branchKey) {
        log.info("Fetching branch capacity for key: {}", branchKey);
        Branch branch = branchRepository.findByBranchKey(branchKey);
        if (branch == null) {
            log.warn("Branch not found with key: {}", branchKey);
            throw new IllegalArgumentException("Branch not found with key: " + branchKey);
        }

        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        Set<Status> statuses = Set.of(
                Status.SCHEDULED,
                Status.HOLD,
                Status.TRANSFERRED_TO_SERVICE,
                Status.TRANSFERRED_TO_COUNTER,
                Status.TRANSFERRED_TO_USER,
                Status.ATTENDING
        );

        log.info("Calculating capacity between {} and {}", start, end);

        List<Assistance> assistances = assistanceRepository.findByCreatedAtBetweenAndBranchAndStatusIn(start, end, branchKey, statuses);
        List<Counter> counters = counterRepository.findByBranchAndInUse(branch.getId(), true);

        return new Capacity(branchKey, assistances.size(), counters.size());
    }
}