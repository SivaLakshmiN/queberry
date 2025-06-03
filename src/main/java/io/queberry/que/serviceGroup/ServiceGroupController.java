package io.queberry.que.serviceGroup;

import io.queberry.que.dto.AddServiceToServiceGroup;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ServiceGroupController {

    private final ServiceGroupService serviceGroupService;

    public ServiceGroupController(ServiceGroupService serviceGroupService) {
        this.serviceGroupService = serviceGroupService;
    }

    @PutMapping("/serviceGroups/{serviceGroup_id}/services")
    public ResponseEntity<?> addToServiceGroups(@PathVariable("serviceGroup_id") String serviceGroupId,
                                                @RequestBody AddServiceToServiceGroup resource) {
        ServiceGroup updated = serviceGroupService.addServicesToGroup(serviceGroupId, resource);
        return ResponseEntity.ok("Update SubGroup Successfully");
    }
    @GetMapping("/serviceGroups")
    public ResponseEntity<?> getAllServiceGroup() {
        Page<ServiceGroup> serviceList = serviceGroupService.getAllServiceGroups();
        return ResponseEntity.ok(serviceList);
    }
    @PostMapping("/serviceGroups")
    public ResponseEntity<?> save(@RequestBody ServiceGroup serviceGroup) {
        serviceGroup = serviceGroupService.save(serviceGroup);
        return ResponseEntity.ok("Saved Successfully");
    }
    @PutMapping("/serviceGroups/{id}")
    public ResponseEntity<?> edit(@PathVariable("id") String id, @RequestBody ServiceGroupRequest serviceGroupRequest) {
        ServiceGroup updated = serviceGroupService.edit(id, serviceGroupRequest);
        return ResponseEntity.ok("Update SubGroup Successfully");
    }
}
