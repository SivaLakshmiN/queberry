package io.queberry.que.service.impl;

import io.queberry.que.dto.AddServiceToServiceGroup;
import io.queberry.que.dto.ServiceGroupDTO;
import io.queberry.que.dto.ServiceGroupRequest;
import io.queberry.que.entity.ServiceGroup;
import io.queberry.que.exception.DataNotFoundException;
import io.queberry.que.exception.QueueException;
import io.queberry.que.repository.ServiceGroupRepository;
import io.queberry.que.repository.ServiceRepository;
import io.queberry.que.service.ServiceGroupService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
        ServiceGroup serviceGroup = serviceGroupRepository.findById(serviceGroupId)
                .orElseThrow(() -> new EntityNotFoundException("ServiceGroup not found with id: " + serviceGroupId));

        Set<String> serviceIds = resource.getServices();
        Set<String> validServiceIds = serviceIds.stream()
                .map(id -> serviceRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Service not found with id: " + id)))
                .map(io.queberry.que.entity.Service::getId)
                .collect(Collectors.toSet());

        serviceGroup.clearServices();
        serviceGroup.setServices(validServiceIds);
        return serviceGroupRepository.save(serviceGroup);
    }

    @Override
    public Page<ServiceGroup> getAllServiceGroups() {
        return (Page<ServiceGroup>) serviceGroupRepository.findAll();
    }
    @Override
    public ServiceGroup save(ServiceGroup resource) {
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
            throw new QueueException(errorMsg.toString().trim(), HttpStatus.PRECONDITION_FAILED);
        }
        return serviceGroupRepository.save(serviceGroup);
    }
    @Override
    public ServiceGroup edit(String id, ServiceGroupRequest serviceGroupRequest) {
        ServiceGroup existing = serviceGroupRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("ServiceGroup not found with id: " + id));

        existing.setDisplayName(serviceGroupRequest.getDisplayName());
        existing.setNames(serviceGroupRequest.getNames());
        existing.setServices(new HashSet<>(serviceRepository.findByIdIn(serviceGroupRequest.getServices())));

        return serviceGroupRepository.save(existing);
    }
}
