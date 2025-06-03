package io.queberry.que.serviceGroup;

import io.queberry.que.dto.AddServiceToServiceGroup;
import org.springframework.data.domain.Page;

public interface ServiceGroupService {

    ServiceGroup addServicesToGroup(String serviceGroupId, AddServiceToServiceGroup resource);
    Page<ServiceGroup> getAllServiceGroups();
    ServiceGroup save(ServiceGroup resource);
    ServiceGroup edit(String id, ServiceGroupRequest serviceGroupRequest);
}
