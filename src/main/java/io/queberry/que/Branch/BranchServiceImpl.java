package io.queberry.que.service.impl;

import io.queberry.que.Assistance.Assistance;
import io.queberry.que.Branch.*;
import io.queberry.que.Counter.Counter;
import io.queberry.que.Employee.Employee;
import io.queberry.que.Role.Role;
import io.queberry.que.Service.ServiceService;
import io.queberry.que.ServiceGroup.ServiceGroup;
import io.queberry.que.ServiceGroup.ServiceGroupDTO;
import io.queberry.que.dto.Capacity;
import io.queberry.que.ServiceGroup.ServiceGroupRequest;
import io.queberry.que.enums.Status;
import io.queberry.que.exception.DataNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Override
    public Page<Branch> getBranchesByRegion(String regionId, Pageable pageable) {
        return branchRepository.findByRegion(regionId, pageable);
    }

    @Override
    public Branch activateBranch(String branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new DataNotFoundException("Branch not found with id " + branchId));
        branch.setActive(true);
        branch = branchRepository.save(branch);

        Set<Branch> branches = new HashSet<>();
        branches.add(branch);
//        sequenceEngine.setSequenceManagerByBranches(branches);

        return branch;
    }

    @Override
    public Branch deActivateBranch(String branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new DataNotFoundException("Branch not found with id " + branchId));

        branch.setActive(false);
        branch = branchRepository.save(branch);

//        sequenceEngine.removeBranchFromSequence(branch.getBranchKey());

        Set<String> branches = new HashSet<>();
//        branches.add(branch);
        Set<Employee> employees = employeeRepository.findByBranchesIn(branches);
        for (Employee employee : employees) {
            Set<String> employeeBranches = (Set<String>) employee;
            employeeBranches.remove(branch);
//            employee.setBranch(employeeBranches);
            employeeRepository.save(employee);
        }

        return branch;
    }

    @Override
    public Page<BranchDTO> filterBranchesByName(HttpServletRequest request, String region, String brName, Pageable pageable) {
        String username = request.getUserPrincipal().getName();
        Optional<Employee> employeeOpt = employeeRepository.findEmployeeByUsername(username);

        if (employeeOpt.isEmpty()) {
            return Page.empty(pageable);
        }

        Employee employee = employeeOpt.get();
        Set<Role> roles = employee.getAuthorities();

//        boolean isAdmin = roles.contains(roleRepository.findByName("PRODUCT_ADMIN"))
//                || roles.contains(roleRepository.findByName("ORG_ADMIN"));
//
//        if (isAdmin) {
//            Page<Branch> data = branchRepository.findByRegionAndNameContainingIgnoreCase(region, brName, pageable);
//            return data.map(branchMapper::entityToDto);
//        } else {
//        Set<String> filtered = employee.getBranch().stream()
//                .filter(b -> b.getName() != null && b.getName().toLowerCase().contains(brName.toLowerCase()))
//                .collect(Collectors.toSet());

//        Page<String> branchPage = getPage(filtered, pageable);
//        return branchPage.map(branchMapper::entityToDto);
        return null;
    }

    private Page<Branch> getPage(Set<Branch> branches, Pageable pageable) {
        List<Branch> branchList = branches.stream().collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), branchList.size());

        if (start > end) {
            return Page.empty(pageable);
        }

        List<Branch> subList = branchList.subList(start, end);
        return new PageImpl<>(subList, pageable, branchList.size());
    }

    @Override
    public Branch assignServiceGroup(String branchId, ServiceGroupRequest request) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new DataNotFoundException("Branch not found"));

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
        Branch branch = branchRepository.findByBranchKey(branchKey);
        if (branch == null) {
            throw new RuntimeException("Branch not found with key: " + branchKey);
        }
        return branch.getServiceGroup().stream()
                .map(ServiceGroupDTO::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Capacity getBranchCapacity(String branchKey) {
        Branch branch = branchRepository.findByBranchKey(branchKey);
        if (branch == null) {
            throw new IllegalArgumentException("Branch not found with key: " + branchKey);
        }

        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        Set<Status> statuses = new HashSet<>();
        statuses.add(Status.SCHEDULED);
        statuses.add(Status.HOLD);
        statuses.add(Status.TRANSFERRED_TO_SERVICE);
        statuses.add(Status.TRANSFERRED_TO_COUNTER);
        statuses.add(Status.TRANSFERRED_TO_USER);
        statuses.add(Status.ATTENDING);

        List<Assistance> assistances = assistanceRepository
                .findByCreatedAtBetweenAndBranchAndStatusIn(start, end, branchKey, statuses);

        List<Counter> counters = counterRepository
                .findByBranchAndInUse(branch.getId(), true);

        return new Capacity(branchKey, assistances.size(), counters.size());
    }
}