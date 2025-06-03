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
public class ServicesDTO {
    
    private String id;
    private String name;

    public ServicesDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }
}