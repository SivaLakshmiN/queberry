package io.queberry.que.service;

import io.queberry.que.dto.*;
import io.queberry.que.branch.Branch;
import io.queberry.que.entity.Service;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class ServiceController {

    private final ServiceService serviceService;

    @Autowired
    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

//    @PostMapping("/services")
//    public ResponseEntity<Service> createBranch(@RequestBody Service service) {
//        Service services = serviceService.createService(service);
//        return ResponseEntity.ok(services);
//    }

    @GetMapping("/services/active")
    public ResponseEntity<Set<Service>> getActiveServices(
            @RequestParam boolean active) {

        Set<Service> activeServices = serviceService.getAllActiveServices(active);
        return ResponseEntity.ok(activeServices);
    }

    @GetMapping("/services/{region_id}/active")
    public ResponseEntity<Set<ServiceRegionResponse>> getRegionActiveServices(@PathVariable("region_id") String id) {
        return ResponseEntity.ok(serviceService.getRegionActiveServices(id));
    }

    @PutMapping("/services/{regionId}/activate")
    public ResponseEntity<Service> activate(@PathVariable String regionId) {
        Service activated = serviceService.activate(regionId);
        if (activated.isActive()) {
            return ResponseEntity.ok(activated);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping("/services/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable String id) {
        serviceService.deactivate(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/services/{id}")
    public ResponseEntity<?> updateService(
            @PathVariable String id,
            @RequestBody ServiceResource serviceResource) {
        try {
            Service updatedService = serviceService.updateService(id, serviceResource);
            return ResponseEntity.ok("Service Updated Successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/services/{id}/subservices/add")
    public ResponseEntity<?> subservices(
            @PathVariable("id") String serviceId,
            @RequestBody Set<String> subServices) {

        Service updated = serviceService.subServices(serviceId, subServices);
        return ResponseEntity.ok("Service Updated Successfully");
    }

    @PutMapping("/services/{id}/subServices/deactivate/{subService_id}")
    public ResponseEntity<?> deactivateSubServices(
            @PathVariable("id") String serviceId,
            @PathVariable("subService_id") String subServiceId) {

        Service updated = serviceService.deactivateSubServices(serviceId, subServiceId);
        return ResponseEntity.ok("Service Updated Successfully");
    }

    @GetMapping("/services/{region_id}")
    public ResponseEntity<?> getAllRegionServices(@PathVariable("region_id") String regionId, Pageable pageable) {
        Service service = serviceService.getAllRegionServices(regionId, pageable);
        return ResponseEntity.ok(service);
    }

    @GetMapping("/services")
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        List<ServiceDTO> services = serviceService.getAllServices();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/services/{id}/filterByName")
    public ResponseEntity<?> filterByName(
            @PathVariable("id") String regionId,
            @RequestParam("service") String serviceName,
            Pageable pageable) {

        Page<Service> services = serviceService.filterByName(regionId, serviceName, pageable);
        return ResponseEntity.ok(services);
    }
    @GetMapping("/services-by-sub-trans-group/{id}")
    public ResponseEntity<Set<ServiceDTO>> findServicesBySubTransactionGroup(@PathVariable("id") String subTransactionGroupId) {
        Set<ServiceDTO> serviceDTOS = serviceService.findBySubServiceGroup(subTransactionGroupId);
        return ResponseEntity.ok(serviceDTOS);
    }
    @PostMapping("/services")
    public ResponseEntity<?> save(@RequestBody Service service, HttpServletRequest request) {
        Service s = serviceService.createService(service, request);
        return ResponseEntity.ok("Service Created Successfully");
    }
    @GetMapping("/services/appointmentServices/{branch_id}")
    public ResponseEntity<?> getBranchServices(@PathVariable("branch_id") Branch branch) {
        try {
            Set<ServiceResponse> services = serviceService.getBranchServices(branch);
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching services");
        }
    }
    @PutMapping("/services/appointmentServices")
    public ResponseEntity<?> getBranchService(@RequestBody ApptServiceResource apptServiceResource) {
        try {
            Set<ServiceResponse> services = serviceService.getBranchService(apptServiceResource);
            return ResponseEntity.ok("Service Updated Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve services.");
        }
    }
}
