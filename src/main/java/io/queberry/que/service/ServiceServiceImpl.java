package io.queberry.que.service;

//import io.queberry.que.config.JwtTokenUtil;
//import io.queberry.que.config.RedisSequenceEngine;
import io.queberry.que.AuditLogs.AuditLogs;
import io.queberry.que.AuditLogs.AuditLogsRepository;
import io.queberry.que.Branch.Branch;
import io.queberry.que.Branch.BranchRepository;
import io.queberry.que.NewSlot.NewSlot;
import io.queberry.que.NewSlot.NewSlotRepository;
import io.queberry.que.Region.Region;
import io.queberry.que.Region.RegionRepository;
import io.queberry.que.SharedSequence.SharedSequence;
import io.queberry.que.SharedSequence.SharedSequenceRepository;
import io.queberry.que.SubService.SubService;
import io.queberry.que.SubService.SubServiceRepository;
import io.queberry.que.SubTransaction.SubTransactionGroupRepository;
import io.queberry.que.dto.*;
import io.queberry.que.dto.ServiceResource;
import io.queberry.que.dto.ServiceResponse;
import io.queberry.que.entity.Service;
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
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    private final SharedSequenceRepository sharedSequenceRepository;

    private final RegionRepository regionRepository;

    private final SubServiceRepository subServiceRepository;

    private final AuditLogsRepository auditLogsRepository;

    private final ServiceEngine serviceEngine;

//    private final JwtTokenUtil jwtTokenUtil;

    private final BranchRepository branchRepository;

    private final NewSlotRepository newSlotRepository;

//    @Autowired
//    RedisSequenceEngine sequenceEngine;

    private final SubTransactionGroupRepository subTransactionGroupRepository;

    public ServiceServiceImpl(ServiceRepository serviceRepository, SharedSequenceRepository sharedSequenceRepository, RegionRepository regionRepository, SubServiceRepository subServiceRepository, AuditLogsRepository auditLogsRepository, ServiceEngine serviceEngine, BranchRepository branchRepository, NewSlotRepository newSlotRepository,SubTransactionGroupRepository subTransactionGroupRepository) {
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
        return serviceRepository.findByActiveTrue(Sort.by(Sort.Order.asc("name")));
    }

    @Override
    public Set<ServiceRegionResponse> getRegionActiveServices(String regionId) {
        return serviceRepository.findByIdAndActiveTrue(regionId, Sort.by(Sort.Order.asc("name")));
    }

    @Override
    public Service activate(String id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        service.setActive(true);
        return serviceRepository.save(service);
    }

    @Override
    public Service deactivate(String id) {
        serviceRepository.findById(id).ifPresent(service -> {
            service.setActive(false);
            serviceRepository.save(service);
        });
        return null;
    }

    @Override
    public Service updateService(String id, ServiceResource serviceResource) {
        return serviceRepository.findById(id)
                .map(existing -> {
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

                    Region region = serviceResource.getRegion();
                    if (region != null && region.getId() != null) {
                        String managedRegion = String.valueOf(regionRepository.findById(region.getId())
                                .orElseThrow(() -> new RuntimeException("Region not found")));
                        existing.setRegion(managedRegion);
                    }
                    SharedSequence sharedSequence = serviceResource.getSharedSequence();
                    if (sharedSequence != null && sharedSequence.getId() != null) {
                        String managedSharedSeq = String.valueOf(sharedSequenceRepository.findById(sharedSequence.getId())
                                .orElseThrow(() -> new RuntimeException("Shared Sequence not found")));
                        existing.setSharedSequence(managedSharedSeq);
                    }
                    Set<String> subServiceGroup = serviceResource.getSubServiceGroup();
                    if (subServiceGroup != null && !subServiceGroup.isEmpty()) {
                        existing.setSubServiceGroup(subServiceGroup.toString());
                    }

                    return serviceRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }

    @Override
    public Service subServices(String serviceId, Set<String> subServices) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        return serviceEngine.manageSubServices(service, subServices);
    }

    @Override
    public Service deactivateSubServices(String serviceId, String subserviceId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        if (!service.getSubServices().contains(subserviceId)) {
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
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new RuntimeException("Region not found"));

        Page<Service> servicePage = serviceRepository.findByRegion(region, pageable);

        return servicePage.getContent().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No services found for region"));
    }
    @Override
    public List<ServiceDTO> getAllServices() {
        return serviceRepository.findAllService();
    }

    @Override
    public Page<Service> filterByName(String regionId, String serviceName, Pageable pageable) {
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new RuntimeException("Region not found"));

        return serviceRepository.findByRegionAndNameContainingIgnoreCase(region, serviceName, pageable);
    }

    @Override
    public Set<ServiceDTO> findBySubServiceGroup(String subTransactionGroupId) {
        String group = String.valueOf(subTransactionGroupRepository.findById(subTransactionGroupId)
                .orElseThrow(() -> new RuntimeException("SubTransactionGroup not found")));

        Set<String> services = serviceRepository.findBySubServiceGroup(group);

        return services.stream()
                .map(ServiceDTO::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Service createService(Service service, HttpServletRequest request) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Service>> errors = validator.validate(service);

        if (!errors.isEmpty()) {
            String errorMsg = errors.stream()
                    .map(ConstraintViolation::getMessageTemplate)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException(errorMsg);
        }

        if (service.getSequenceEnd() < service.getSequenceStart()) {
            throw new IllegalArgumentException("Sequence End should be greater than Sequence start.");
        }

        Service savedService = serviceRepository.save(service);

        if (savedService.getName() != null) {
            addServiceToBranch(savedService);
        }

//        String token = request.getHeader("Authorization").substring(7);
//        if (jwtTokenUtil.isTokenExpired(token)) {
//            throw new SecurityException("JWT token expired");
//        }

//        String username = jwtTokenUtil.getUsernameFromToken(token);

        AuditLogs log = new AuditLogs();
        log.setEntityName("Service");
        log.setEntityId(savedService.getId());
        log.setNewData("New service created");
//        log.setCreatedBy(username);
        auditLogsRepository.save(log);

        return savedService;
    }

    private void addServiceToBranch(Service service) {
        Set<Branch> branches = branchRepository.findByGlobalServicesTrueAndActiveTrueAndRegion(service.getRegion());

        for (Branch branch : branches) {
            Set<String> services = branch.getServices();
            services.add("");
            branch.setServices(services);
            branchRepository.save(branch);
        }
//        sequenceEngine.setSequenceManagerByBranches(branches);
    }
    @Override
    public Set<ServiceResponse> getBranchServices(Branch branch) {
        List<NewSlot> slotInfos = newSlotRepository.findByBranchIsAndInputDateBetweenFromDateAndToDate(branch, LocalDate.now());

//        log.info("slots count: {}", slotInfos.size());
//        Set<ServiceResponse> responseSet = slotInfos.stream()
//                .map(slot -> modelMapper.map(slot.getService(), ServiceResponse.class))
//                .collect(Collectors.toSet());

//        log.info("unique services count: {}", responseSet.size());
//        return responseSet;
        return Set.of();
    }
    @Override
    public Set<ServiceResponse> getBranchService(ApptServiceResource apptServiceResource) {
        String branchKey = apptServiceResource.getBranchKey();
        boolean isVirtual = apptServiceResource.isVirtual();

        log.info("request: {}, {}", branchKey, isVirtual);

        Set<Service> services;
        if (isVirtual) {
            log.info("virtual appt");
            services = serviceRepository.findByActiveTrueAndVirtualServiceTrue();
        } else {
            log.info("f2f appt");
            services = serviceRepository.findByActiveTrueAndVirtualServiceFalseOrVirtualServiceNull();
        }

        log.info("service size: {}", services.size());

        Set<NewSlot> slotInfos = new HashSet<>(newSlotRepository.findByBranchAndServiceInAndToDateGreaterThanEqual(
                branchKey, services, LocalDate.now()));

        log.info("slots count: {}", slotInfos.size());

//        Set<ServiceResponse> res = slotInfos.stream()
//                .map(slot -> modelMapper.map(slot.getService(), ServiceResponse.class))
//                .collect(Collectors.toSet());

//        log.info("unique service responses: {}", res.size());
//
//        return res;
        return Set.of();
    }
}