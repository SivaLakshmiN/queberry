package io.queberry.que.service;

import io.queberry.que.entity.Service;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceDTO {
    private String id;
    private String name;
    private String displayName;
    private String sharedSequence;

    public ServiceDTO(String id, String name, String displayName, String sharedSequence) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.sharedSequence = sharedSequence;
    }

    public ServiceDTO(Service service) {
        this.id = service.getId();
        this.name = service.getName();
        this.displayName = service.getDisplayName();
        this.sharedSequence = service.getSharedSequence();
    }
    public ServiceDTO() {
    }
    public ServiceDTO(String s) {
    }

    public ServiceDTO(ServiceDTO serviceDTO) {
    }
}

