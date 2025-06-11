package io.queberry.que.service;
//import io.queberry.que.config.JwtTokenUtil;
//import io.queberry.que.config.RedisSequenceEngine;
import io.queberry.que.auditLogs.AuditLogs;
import io.queberry.que.auditLogs.AuditLogsRepository;
import io.queberry.que.branch.Branch;
import io.queberry.que.branch.BranchRepository;
import io.queberry.que.newSlot.NewSlot;
import io.queberry.que.newSlot.NewSlotRepository;
import io.queberry.que.region.Region;
import io.queberry.que.region.RegionRepository;
import io.queberry.que.sharedSequence.SharedSequence;
import io.queberry.que.sharedSequence.SharedSequenceRepository;
import io.queberry.que.subService.SubService;
import io.queberry.que.subService.SubServiceRepository;
import io.queberry.que.subTransaction.SubTransactionGroupRepository;
import io.queberry.que.dto.*;
import io.queberry.que.dto.ServiceResource;
import io.queberry.que.dto.ServiceResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Slf4j
@org.springframework.stereotype.Service
public class ServiceServiceImpl implements io.queberry.que.service.ServiceService {

    private final io.queberry.que.service.ServiceRepository serviceRepository;

    private final SharedSequenceRepository sharedSequenceRepository;

    private final RegionRepository regionRepository;

    private final SubServiceRepository subServiceRepository;

    private final AuditLogsRepository auditLogsRepository;

    private final io.queberry.que.service.ServiceEngine serviceEngine;

//    private final JwtTokenUtil jwtTokenUtil;

    private final BranchRepository branchRepository;

    private final NewSlotRepository newSlotRepository;

//    @Autowired
//    RedisSequenceEngine sequenceEngine;

    private final SubTransactionGroupRepository subTransactionGroupRepository;

    public ServiceServiceImpl(io.queberry.que.service.ServiceRepository serviceRepository, SharedSequenceRepository sharedSequenceRepository, RegionRepository regionRepository, SubServiceRepository subServiceRepository, AuditLogsRepository auditLogsRepository, io.queberry.que.service.ServiceEngine serviceEngine, BranchRepository branchRepository, NewSlotRepository newSlotRepository, SubTransactionGroupRepository subTransactionGroupRepository) {
        this.serviceRepository = serviceRepository;
        this.sharedSequenceRepository = sharedSequenceRepository;
        this.regionRepository = regionRepository;
        this.subServiceRepository = subServiceRepository;
        this.auditLogsRepository = auditLogsRepository;
        this.serviceEngine = serviceEngine;
//        this.jwtTokenUtil = jwtTokenUtil;
        this.branchRepository = branchRepository;
        this.newSlotRepository = newSlotRepository;
        this.subTransactionGroupRepository = subTransactionGroupRepository;
    }

    @Override
    public Set<Service> getAllActiveServices(boolean active) {
        log.info("Fetching all active services. Expected active = {}", active);
        return serviceRepository.findByActiveTrue(Sort.by(Sort.Order.asc("name")));
    }

    @Override
    public Set<ServiceRegionResponse> getRegionActiveServices(String regionId) {
        log.info("Fetching active services for region ID: {}", regionId);
        return serviceRepository.findByIdAndActiveTrue(regionId, Sort.by(Sort.Order.asc("name")));
    }

    @Override
    public Service activate(String id) {
        log.info("Activating service with ID: {}", id);
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        service.setActive(true);
        return serviceRepository.save(service);
    }

    @Override
    public Service deactivate(String id) {
        log.info("Deactivating service with ID: {}", id);
        serviceRepository.findById(id).ifPresent(service -> {
            service.setActive(false);
            serviceRepository.save(service);
        });
        return null;
    }

    @Override
    public Service updateService(String id, ServiceResource serviceResource) {
        log.info("Updating service with ID: {}", id);
        return serviceRepository.findById(id)
                .map(existing -> {
                    log.info("Existing service found. Updating fields...");
                    existing.setDisplayName(serviceResource.getDisplayName());
                    existing.setDescription(serviceResource.getDescription());
                    existing.setLongDescription(serviceResource.getLongDescription());
                    existing.setActive(serviceResource.isActive());
                    existing.setSequenceStart(serviceResource.getSequenceStart());
                    existing.setSequenceEnd(serviceResource.getSequenceEnd());
                    existing.setSeperator(serviceResource.getSeperator());
                    existing.setNumberOfTickets(serviceResource.getNumberOfTickets());
                    existing.setNames(serviceResource.getNames());
                    existing.setMessage(serviceResource.getMessage());
                    existing.setServeSla(serviceResource.getServeSla());
                    existing.setWaitSla(serviceResource.getWaitSla());
                    existing.setSharedSeq(serviceResource.isSharedSeq());
                    existing.setFormat(serviceResource.isFormat());
                    existing.setApptSlotDuration(serviceResource.getApptSlotDuration());
                    existing.setPriorCancelHrs(serviceResource.getPriorCancelHrs());
                    existing.setPriorBookingDays(serviceResource.getPriorBookingDays());
                    existing.setVirtualService(serviceResource.getVirtualService());

                    if (serviceResource.getRegion() != null && serviceResource.getRegion().getId() != null) {
                        log.info("Validating region ID: {}", serviceResource.getRegion().getId());
                        String managedRegion = String.valueOf(regionRepository.findById(serviceResource.getRegion().getId())
                                .orElseThrow(() -> new RuntimeException("Region not found")));
                        existing.setRegion(managedRegion);
                    }

                    if (serviceResource.getSharedSequence() != null && serviceResource.getSharedSequence().getId() != null) {
                        log.info("Validating shared sequence ID: {}", serviceResource.getSharedSequence().getId());
                        String managedSharedSeq = String.valueOf(sharedSequenceRepository.findById(serviceResource.getSharedSequence().getId())
                                .orElseThrow(() -> new RuntimeException("Shared Sequence not found")));
                        existing.setSharedSequence(managedSharedSeq);
                    }

                    if (serviceResource.getSubServiceGroup() != null && !serviceResource.getSubServiceGroup().isEmpty()) {
                        log.info("Setting sub-service groups: {}", serviceResource.getSubServiceGroup());
                        existing.setSubServiceGroup(serviceResource.getSubServiceGroup().toString());
                    }

                    return serviceRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }

    @Override
    public Service subServices(String serviceId, Set<String> subServices) {
        log.info("Assigning subservices to service ID: {}", serviceId);
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        return serviceEngine.manageSubServices(service, subServices);
    }

    @Override
    public Service deactivateSubServices(String serviceId, String subserviceId) {
        log.info("Deactivating subservice ID: {} from service ID: {}", subserviceId, serviceId);
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        if (!service.getSubServices().contains(subserviceId)) {
            log.warn("SubService ID: {} not associated with service ID: {}", subserviceId, serviceId);
            throw new RuntimeException("SubService ID not associated with this service");
        }

        SubService subService = subServiceRepository.findById(subserviceId)
                .orElseThrow(() -> new RuntimeException("SubService not found"));

        subService.deactivate();
        subServiceRepository.save(subService);
        return service;
    }

    @Override
    public Service getAllRegionServices(String regionId, Pageable pageable) {
        log.info("Fetching all services for region ID: {}", regionId);
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new RuntimeException("Region not found"));

        Page<Service> servicePage = serviceRepository.findByRegion(region, pageable);
        return servicePage.getContent().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No services found for region"));
    }

    @Override
    public List<ServiceDTO> getAllServices() {
        log.info("Fetching all services");
        return serviceRepository.findAllService();
    }

    @Override
    public Page<Service> filterByName(String regionId, String serviceName, Pageable pageable) {
        log.info("Filtering services by name '{}' in region ID: {}", serviceName, regionId);
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new RuntimeException("Region not found"));
        return serviceRepository.findByRegionAndNameContainingIgnoreCase(region, serviceName, pageable);
    }

    @Override
    public Set<ServiceDTO> findBySubServiceGroup(String subTransactionGroupId) {
        log.info("Fetching services by sub-transaction group ID: {}", subTransactionGroupId);
        String group = String.valueOf(subTransactionGroupRepository.findById(subTransactionGroupId)
                .orElseThrow(() -> new RuntimeException("SubTransactionGroup not found")));

        Set<String> services = serviceRepository.findBySubServiceGroup(group);
        return services.stream().map(ServiceDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Service createService(Service service, HttpServletRequest request) {
        log.info("Creating service: {}", service.getName());

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Service>> errors = validator.validate(service);

        if (!errors.isEmpty()) {
            String errorMsg = errors.stream()
                    .map(ConstraintViolation::getMessageTemplate)
                    .collect(Collectors.joining(", "));
            log.warn("Validation errors while creating service: {}", errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        if (service.getSequenceEnd() < service.getSequenceStart()) {
            log.warn("Invalid sequence range: start={}, end={}", service.getSequenceStart(), service.getSequenceEnd());
            throw new IllegalArgumentException("Sequence End should be greater than Sequence start.");
        }

        Service savedService = serviceRepository.save(service);
        log.info("Service saved: {}", savedService.getId());

        if (savedService.getName() != null) {
            addServiceToBranch(savedService);
        }

        AuditLogs logEntry = new AuditLogs();
        logEntry.setEntityName("Service");
        logEntry.setEntityId(savedService.getId());
        logEntry.setNewData("New service created");
        auditLogsRepository.save(logEntry);

        return savedService;
    }

    private void addServiceToBranch(Service service) {
        log.info("Adding service to all active branches in region: {}", service.getRegion());
        Set<Branch> branches = branchRepository.findByGlobalServicesTrueAndActiveTrueAndRegion(service.getRegion());

        for (Branch branch : branches) {
            Set<String> services = branch.getServices();
            services.add(""); // This might need correction if "" is placeholder
            branch.setServices(services);
            branchRepository.save(branch);
            log.info("Service added to branch: {}", branch.getId());
        }
        // sequenceEngine.setSequenceManagerByBranches(branches);
    }

    @Override
    public Set<ServiceResponse> getBranchServices(Branch branch) {
        log.info("Fetching branch services for branch ID: {}", branch.getId());
        List<NewSlot> slotInfos = newSlotRepository.findByBranchIsAndInputDateBetweenFromDateAndToDate(branch, LocalDate.now());
        log.info("Slot count found: {}", slotInfos.size());
        return Set.of(); // Stubbed out
    }

    @Override
    public Set<ServiceResponse> getBranchService(ApptServiceResource apptServiceResource) {
        String branchKey = apptServiceResource.getBranchKey();
        boolean isVirtual = apptServiceResource.isVirtual();
        log.info("Fetching appointment services for branch: {}, isVirtual: {}", branchKey, isVirtual);

        Set<Service> services;
        if (isVirtual) {
            log.info("Virtual appointment services");
            services = serviceRepository.findByActiveTrueAndVirtualServiceTrue();
        } else {
            log.info("Face-to-face appointment services");
            services = serviceRepository.findByActiveTrueAndVirtualServiceFalseOrVirtualServiceNull();
        }

        log.info("Found services: {}", services.size());
        Set<NewSlot> slotInfos = new HashSet<>(newSlotRepository.findByBranchAndServiceInAndToDateGreaterThanEqual(
                branchKey, services, LocalDate.now()));
        log.info("Slots found: {}", slotInfos.size());
        return Set.of(); // Stubbed out
    }
}