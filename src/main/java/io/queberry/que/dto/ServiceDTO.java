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

    public ServiceDTO() {
        
    }

    public ServiceDTO(String s) {
    }

    public ServiceDTO(Service service) {
    }
}

