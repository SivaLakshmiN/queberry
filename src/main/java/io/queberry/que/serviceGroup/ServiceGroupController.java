package io.queberry.que.serviceGroup;

import io.queberry.que.dto.AddServiceToServiceGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api")
public class ServiceGroupController {

    private final ServiceGroupService serviceGroupService;

    public ServiceGroupController(ServiceGroupService serviceGroupService) {
        this.serviceGroupService = serviceGroupService;
    }

    @PutMapping("/{serviceGroup_id}/services")
    public ResponseEntity<?> addToServiceGroups(@PathVariable("serviceGroup_id") String serviceGroupId,
                                                @RequestBody AddServiceToServiceGroup resource) {
        log.info("Adding services to service group with ID: {}", serviceGroupId);
        ServiceGroup updated = serviceGroupService.addServicesToGroup(serviceGroupId, resource);
        log.info("Successfully updated service group ID: {}", serviceGroupId);
        return ResponseEntity.ok("Update SubGroup Successfully");
    }

    @GetMapping
    public ResponseEntity<?> getAllServiceGroup() {
        log.info("Fetching all service groups");
        Page<ServiceGroup> serviceList = serviceGroupService.getAllServiceGroups();
        log.info("Found {} service groups", serviceList.getTotalElements());
        return ResponseEntity.ok(serviceList);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ServiceGroup serviceGroup) {
        log.info("Saving new service group: {}", serviceGroup.getName());
        serviceGroup = serviceGroupService.save(serviceGroup);
        log.info("Service group saved with ID: {}", serviceGroup.getId());
        return ResponseEntity.ok("Saved Successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable("id") String id, @RequestBody ServiceGroupRequest serviceGroupRequest) {
        log.info("Editing service group with ID: {}", id);
        ServiceGroup updated = serviceGroupService.edit(id, serviceGroupRequest);
        log.info("Service group updated successfully with ID: {}", id);
        return ResponseEntity.ok("Update SubGroup Successfully");
    }
}
