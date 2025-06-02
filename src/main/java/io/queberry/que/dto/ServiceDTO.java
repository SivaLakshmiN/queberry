package io.queberry.que.dto;

import io.queberry.que.entity.Service;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ServiceDTO {
    private String id;
    private String name;
    private String displayName;
    private String sharedSequenceName;
    private String status;
    public ServiceDTO(Service service){
        this.id = service.getId();
        this.name = service.getName();
        this.displayName = service.getDisplayName();
        this.sharedSequenceName = service.getSharedSequence();
        this.status = service.toString();
    }

    public ServiceDTO(String s) {
    }
}

