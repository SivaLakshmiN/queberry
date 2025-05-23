package io.queberry.que.dto;

import io.queberry.que.entity.Service;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import lombok.*;
import java.util.HashMap;
import java.util.Map;
@Setter
@Getter
@ToString
@NoArgsConstructor(force = true)
@EqualsAndHashCode
public class ServiceDTO {
    private String id;
    private String name;
    private String displayName;
    @Column(columnDefinition = "nvarchar(255)")
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String,String> names = new HashMap<>(0);
    private int apptSlotDuration=0;

    public ServiceDTO(Service service){
        this.id = service.getId();
        this.name = service.getName();
        this.displayName = service.getDisplayName();
        this.names = service.getNames();
        this.apptSlotDuration = service.getApptSlotDuration();
    }

    public ServiceDTO(String s) {
    }
}

