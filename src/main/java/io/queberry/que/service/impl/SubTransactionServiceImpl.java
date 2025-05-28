package io.queberry.que.service.impl;

import io.queberry.que.dto.SubTransactionDTO;
import io.queberry.que.dto.SubTransactionGroupDTO;
import io.queberry.que.dto.SubTransactionGroupReq;
import io.queberry.que.entity.SubTransaction;
import io.queberry.que.entity.SubTransactionGroup;
import io.queberry.que.exception.DataNotFoundException;
import io.queberry.que.repository.ServiceRepository;
import io.queberry.que.repository.SubTransactionGroupRepository;
import io.queberry.que.repository.SubTransactionRepository;
import io.queberry.que.service.SubTransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SubTransactionServiceImpl implements SubTransactionService {

    private final SubTransactionRepository subTransactionRepository;

    private final SubTransactionGroupRepository subTransactionGroupRepository;

    private final ServiceRepository serviceRepository;

    public SubTransactionServiceImpl(SubTransactionRepository subTransactionRepository, SubTransactionGroupRepository subTransactionGroupRepository, ServiceRepository serviceRepository) {
        this.subTransactionRepository = subTransactionRepository;
        this.subTransactionGroupRepository = subTransactionGroupRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public Page<SubTransactionDTO> getSubTransactionsByRegion(String regionId, Pageable pageable) {
        Page<SubTransaction> subTransactions = subTransactionRepository.findByRegion(regionId, pageable);
        return subTransactions.map(SubTransactionDTO::new);
    }
    @Override
    public Set<SubTransactionDTO> getAllSubTransactionsByRegion(String regionId) {
        Set<SubTransaction> subTransactions = subTransactionRepository.findByRegion(regionId, Sort.by(Sort.Order.asc("name")));
        return subTransactions.stream()
                .map(SubTransactionDTO::new)
                .collect(Collectors.toSet());
    }
    @Override
    public Page<SubTransactionDTO> getFilterSubTransaction(String region, String name, Pageable pageable) {
        Page<SubTransaction> filtered = subTransactionRepository.findByRegionAndNameContainingIgnoreCase(region, name, pageable);
        return filtered.map(SubTransactionDTO::new);
    }
    @Override
    public SubTransaction save(SubTransaction subTransaction) {
        return subTransactionRepository.save(subTransaction);
    }
    @Override
    public SubTransaction edit(String id, SubTransaction editSubTransaction) {
        SubTransaction existing = subTransactionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("SubTransaction not found with id: " + id));

        existing.setName(editSubTransaction.getName());
        existing.setDisplayName(editSubTransaction.getDisplayName());
        existing.setInput(editSubTransaction.isInput());
        existing.setInputType(editSubTransaction.getInputType());
        existing.setSelectionValues(editSubTransaction.getSelectionValues());
        existing.setActive(editSubTransaction.isActive());
        existing.setRegion(editSubTransaction.getRegion());
        existing.setSecondaryName(editSubTransaction.getSecondaryName());

        return subTransactionRepository.save(existing);
    }
    @Override
    public Page<SubTransactionGroupDTO> getSubTransactionGroupsByRegion(String region, Pageable pageable) {
        Page<SubTransactionGroup> subTransactionGroups = subTransactionGroupRepository.findByRegion(region, pageable);
        return subTransactionGroups.map(SubTransactionGroupDTO::new);
    }
    @Override
    public Page<SubTransactionGroupDTO> filterSubTransactionGroupsByRegionAndName(String region, String name, Pageable pageable) {
        Page<SubTransactionGroup> groups = subTransactionGroupRepository.findByRegionAndNameContainingIgnoreCase(region, name, pageable);
        return groups.map(SubTransactionGroupDTO::new);
    }
    @Override
    public SubTransactionGroup saveSubTransactionGroup(SubTransactionGroupReq stgReq) {
        List<SubTransaction> subTransactions = subTransactionRepository.findAllById(stgReq.getSubTransactions());

        SubTransactionGroup subTransactionGroup = new SubTransactionGroup();
//        subTransactionGroup.setSubTransactions(stgReq.getSubTransactions());
        subTransactionGroup.setRegion(stgReq.getRegion());
        subTransactionGroup.setName(stgReq.getName());
        subTransactionGroup.setDisplayName(stgReq.getDisplayName());
        subTransactionGroup.setSecondaryName(stgReq.getSecondaryName());
        subTransactionGroup.setActive(stgReq.isActive());

        return subTransactionGroupRepository.save(subTransactionGroup);
    }
    @Override
    public SubTransactionGroup editSubTransactionGroup(String id, SubTransactionGroupReq editReq) {
        SubTransactionGroup subTransactionGroup = subTransactionGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SubTransactionGroup not found with ID: " + id));

        List<SubTransaction> subTransactions = subTransactionRepository.findAllById(editReq.getSubTransactions());

        subTransactionGroup.setName(editReq.getName());
        subTransactionGroup.setDisplayName(editReq.getDisplayName());
        subTransactionGroup.setSecondaryName(editReq.getSecondaryName());
        subTransactionGroup.setActive(editReq.isActive());
//        subTransactionGroup.setSubTransactions(editReq.getSubTransactions());
        subTransactionGroup.setRegion(editReq.getRegion());

        return subTransactionGroupRepository.save(subTransactionGroup);
    }
    @Override
    public void assignSubTransactionGroupToServices(String subTransactionGroupId, Set<String> serviceIds) {
        String group = String.valueOf(subTransactionGroupRepository.findById(subTransactionGroupId)
                .orElseThrow(() -> new RuntimeException("SubTransactionGroup not found")));
        Set<String> currentlyAssignedServiceIds = serviceRepository.findBySubServiceGroup(group);

        for (String serviceId : currentlyAssignedServiceIds) {
            if (!serviceIds.contains(serviceId)) {
                io.queberry.que.entity.Service service = serviceRepository.findById(serviceId)
                        .orElseThrow(() -> new RuntimeException("Service not found"));
                service.setSubServiceGroup(null);
                serviceRepository.save(service);
            }
        }
        for (String serviceId : serviceIds) {
            io.queberry.que.entity.Service service = serviceRepository.findById(serviceId)
                    .orElseThrow(() -> new RuntimeException("Service not found"));
            service.setSubServiceGroup(group);
            serviceRepository.save(service);
        }
    }
}
