package io.queberry.que.service;

import io.queberry.que.dto.AddServiceToServiceGroup;
import io.queberry.que.dto.ServiceGroupRequest;
import io.queberry.que.entity.ServiceGroup;
import org.springframework.data.domain.Page;

public interface ServiceGroupService {

    ServiceGroup addServicesToGroup(String serviceGroupId, AddServiceToServiceGroup resource);
    Page<ServiceGroup> getAllServiceGroups();
    ServiceGroup save(ServiceGroup resource);
    ServiceGroup edit(String id, ServiceGroupRequest serviceGroupRequest);
}
