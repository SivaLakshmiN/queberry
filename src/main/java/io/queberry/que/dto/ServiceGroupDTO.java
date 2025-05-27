package io.queberry.que.dto;

import io.queberry.que.entity.Service;
import io.queberry.que.entity.ServiceGroup;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@Setter
@Getter
@ToString
@NoArgsConstructor(force = true)
@EqualsAndHashCode
public class ServiceGroupDTO {
    private String id;
    private String name;
    private String displayName;
    private Map<String,String> names = new HashMap<>(0);
    private Set<ServiceDTO> services = new HashSet<>(0);

    public ServiceGroupDTO(ServiceGroup serviceGroup){
        this.id = serviceGroup.getId();
        this.name = serviceGroup.getName();
        this.displayName = serviceGroup.getDisplayName();
        this.names = serviceGroup.getNames();
        this.services = getServiceDTO(serviceGroup.getServices());
    }

    public ServiceGroupDTO(String s) {
    }

    private Set<ServiceDTO> getServiceDTO(Set<String> services){
        return services.stream().map(ServiceDTO::new).collect(Collectors.toSet());
    }
}
