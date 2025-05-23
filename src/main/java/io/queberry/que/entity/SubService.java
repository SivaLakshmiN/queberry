package io.queberry.que.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity(name = "que_sub_service")
@Table(name = "que_sub_service")
@Getter
@Setter
@ToString
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = true)
public class SubService extends AuditedEntity {

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    private boolean status = true;
    private String name;
    private String description;

    public SubService(String uuid,String name, String description) {
        setId(uuid);
        this.name = name;
        this.description = description;
        this.status = true;
    }

    public SubService(String uuid,String name, String description, boolean status) {
        setId(uuid);
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public SubService(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = true;
    }

    public SubService deactivate(){
        this.status = false;
        return this;
    }

    public SubService activate(){
        this.status = true;
        return this;
    }
}

