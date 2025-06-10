package io.queberry.que.subTransaction;

import io.queberry.que.exception.DataNotFoundException;
import io.queberry.que.service.ServiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Slf4j
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
        log.info("getSubTransactionsByRegion called with regionId={} and pageable={}", regionId, pageable);
        Page<SubTransaction> subTransactions = subTransactionRepository.findByRegion(regionId, pageable);
        return subTransactions.map(SubTransactionDTO::new);
    }

    @Override
    public Set<SubTransactionDTO> getAllSubTransactionsByRegion(String regionId) {
        log.info("getAllSubTransactionsByRegion called with regionId={}", regionId);
        Set<SubTransaction> subTransactions = subTransactionRepository.findByRegion(regionId, Sort.by(Sort.Order.asc("name")));
        return subTransactions.stream()
                .map(SubTransactionDTO::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Page<SubTransactionDTO> getFilterSubTransaction(String region, String name, Pageable pageable) {
        log.info("getFilterSubTransaction called with region={}, name={}, pageable={}", region, name, pageable);
        Page<SubTransaction> filtered = subTransactionRepository.findByRegionAndNameContainingIgnoreCase(region, name, pageable);
        return filtered.map(SubTransactionDTO::new);
    }

    @Override
    public SubTransaction save(SubTransaction subTransaction) {
        log.info("save called with SubTransaction={}", subTransaction);
        return subTransactionRepository.save(subTransaction);
    }

    @Override
    public SubTransaction edit(String id, SubTransaction editSubTransaction) {
        log.info("edit called with id={}, SubTransaction={}", id, editSubTransaction);
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
        log.info("getSubTransactionGroupsByRegion called with region={}, pageable={}", region, pageable);
        Page<SubTransactionGroup> subTransactionGroups = subTransactionGroupRepository.findByRegion(region, pageable);
        return subTransactionGroups.map(SubTransactionGroupDTO::new);
    }

    @Override
    public Page<SubTransactionGroupDTO> filterSubTransactionGroupsByRegionAndName(String region, String name, Pageable pageable) {
        log.info("filterSubTransactionGroupsByRegionAndName called with region={}, name={}, pageable={}", region, name, pageable);
        Page<SubTransactionGroup> groups = subTransactionGroupRepository.findByRegionAndNameContainingIgnoreCase(region, name, pageable);
        return groups.map(SubTransactionGroupDTO::new);
    }

    @Override
    public SubTransactionGroup saveSubTransactionGroup(SubTransactionGroupReq stgReq) {
        log.info("saveSubTransactionGroup called with request={}", stgReq);
        List<SubTransaction> subTransactions = subTransactionRepository.findAllById(stgReq.getSubTransactions());

        SubTransactionGroup subTransactionGroup = new SubTransactionGroup();
        //subTransactionGroup.setSubTransactions(stgReq.getSubTransactions());
        subTransactionGroup.setRegion(stgReq.getRegion());
        subTransactionGroup.setName(stgReq.getName());
        subTransactionGroup.setDisplayName(stgReq.getDisplayName());
        subTransactionGroup.setSecondaryName(stgReq.getSecondaryName());
        subTransactionGroup.setActive(stgReq.isActive());

        return subTransactionGroupRepository.save(subTransactionGroup);
    }

    @Override
    public SubTransactionGroup editSubTransactionGroup(String id, SubTransactionGroupReq editReq) {
        log.info("editSubTransactionGroup called with id={}, request={}", id, editReq);
        SubTransactionGroup subTransactionGroup = subTransactionGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SubTransactionGroup not found with ID: " + id));

        List<SubTransaction> subTransactions = subTransactionRepository.findAllById(editReq.getSubTransactions());

        subTransactionGroup.setName(editReq.getName());
        subTransactionGroup.setDisplayName(editReq.getDisplayName());
        subTransactionGroup.setSecondaryName(editReq.getSecondaryName());
        subTransactionGroup.setActive(editReq.isActive());
        //subTransactionGroup.setSubTransactions(editReq.getSubTransactions());
        subTransactionGroup.setRegion(editReq.getRegion());

        return subTransactionGroupRepository.save(subTransactionGroup);
    }

    @Override
    public void assignSubTransactionGroupToServices(String subTransactionGroupId, Set<String> serviceIds) {
        log.info("assignSubTransactionGroupToServices called with subTransactionGroupId={}, serviceIds={}", subTransactionGroupId, serviceIds);

        String group = String.valueOf(subTransactionGroupRepository.findById(subTransactionGroupId)
                .orElseThrow(() -> new RuntimeException("SubTransactionGroup not found")));

        Set<String> currentlyAssignedServiceIds = serviceRepository.findBySubServiceGroup(group);

        for (String serviceId : currentlyAssignedServiceIds) {
            if (!serviceIds.contains(serviceId)) {
                io.queberry.que.service.Service service = serviceRepository.findById(serviceId)
                        .orElseThrow(() -> new RuntimeException("Service not found"));
                service.setSubServiceGroup(null);
                serviceRepository.save(service);
                log.info("Removed sub service group from serviceId={}", serviceId);
            }
        }
        for (String serviceId : serviceIds) {
            io.queberry.que.service.Service service = serviceRepository.findById(serviceId)
                    .orElseThrow(() -> new RuntimeException("Service not found"));
            service.setSubServiceGroup(group);
            serviceRepository.save(service);
            log.info("Assigned sub service group '{}' to serviceId={}", group, serviceId);
        }
    }
}
