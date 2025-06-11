package io.queberry.que.serviceGroup;

import io.queberry.que.dto.AddServiceToServiceGroup;
import io.queberry.que.exception.DataNotFoundException;
import io.queberry.que.exception.QueueException;
import io.queberry.que.service.ServiceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
@Slf4j
@Service
public class ServiceGroupServiceImpl implements ServiceGroupService {

    private final ServiceGroupRepository serviceGroupRepository;

    private final ServiceRepository serviceRepository;

    public ServiceGroupServiceImpl(ServiceGroupRepository serviceGroupRepository, ServiceRepository serviceRepository) {
        this.serviceGroupRepository = serviceGroupRepository;
        this.serviceRepository = serviceRepository;
    }
    @Override
    public ServiceGroup addServicesToGroup(String serviceGroupId, AddServiceToServiceGroup resource) {
        log.info("Adding services to service group with ID: {}", serviceGroupId);

        ServiceGroup serviceGroup = serviceGroupRepository.findById(serviceGroupId)
                .orElseThrow(() -> {
                    log.error("ServiceGroup not found with id: {}", serviceGroupId);
                    return new EntityNotFoundException("ServiceGroup not found with id: " + serviceGroupId);
                });

        Set<String> serviceIds = resource.getServices();
        log.info("Received {} service IDs to add", serviceIds.size());

        Set<String> validServiceIds = serviceIds.stream()
                .map(id -> serviceRepository.findById(id)
                        .orElseThrow(() -> {
                            log.error("Service not found with id: {}", id);
                            return new EntityNotFoundException("Service not found with id: " + id);
                        }))
                .map(io.queberry.que.service.Service::getId)
                .collect(Collectors.toSet());

        serviceGroup.clearServices();
        serviceGroup.setServices(validServiceIds);

        ServiceGroup saved = serviceGroupRepository.save(serviceGroup);
        log.info("Service group updated with {} services", validServiceIds.size());
        return saved;
    }

    @Override
    public Page<ServiceGroup> getAllServiceGroups() {
        log.info("Fetching all service groups");
        Page<ServiceGroup> page = (Page<ServiceGroup>) serviceGroupRepository.findAll();
        log.info("Found {} service groups", page.getTotalElements());
        return page;
    }

    @Override
    public ServiceGroup save(ServiceGroup resource) {
        log.info("Saving new service group with name: {}", resource.getName());

        ServiceGroup serviceGroup = new ServiceGroup();
        serviceGroup.setName(resource.getName());
        serviceGroup.setDescription(resource.getDescription());
        serviceGroup.setNames(resource.getNames());
        serviceGroup.setDisplayName(resource.getDisplayName());
        serviceGroup.setServices(resource.getServices());

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<ServiceGroup>> violations = validator.validate(serviceGroup);

        if (!violations.isEmpty()) {
            StringBuilder errorMsg = new StringBuilder();
            for (ConstraintViolation<ServiceGroup> violation : violations) {
                errorMsg.append(violation.getMessageTemplate()).append(" ");
            }
            log.error("Validation failed for service group: {}", errorMsg.toString().trim());
            throw new QueueException(errorMsg.toString().trim(), HttpStatus.PRECONDITION_FAILED);
        }

        ServiceGroup saved = serviceGroupRepository.save(serviceGroup);
        log.info("Service group saved with ID: {}", saved.getId());
        return saved;
    }

    @Override
    public ServiceGroup edit(String id, ServiceGroupRequest serviceGroupRequest) {
        log.info("Editing service group with ID: {}", id);

        ServiceGroup existing = serviceGroupRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ServiceGroup not found with id: {}", id);
                    return new DataNotFoundException("ServiceGroup not found with id: " + id);
                });

        existing.setDisplayName(serviceGroupRequest.getDisplayName());
        existing.setNames(serviceGroupRequest.getNames());
        existing.setServices(new HashSet<>(serviceRepository.findByIdIn(serviceGroupRequest.getServices())));

        ServiceGroup saved = serviceGroupRepository.save(existing);
        log.info("Service group edited successfully with ID: {}", saved.getId());
        return saved;
    }
}
