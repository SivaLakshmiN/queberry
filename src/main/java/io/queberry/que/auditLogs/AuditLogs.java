package io.queberry.que.auditLogs;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@ToString
@Getter
@Setter
public class AuditLogs {

    @Id
    @Column(name = "id")
    protected String id;

    @JsonIgnore
    @Version
    private Long version;

    @CreatedDate
    private LocalDateTime created;

    @CreatedBy
    private String createdBy;

    private String entityName;
    private String entityId;
    private String entityField;
    private String oldData;

    private String newData;

    public AuditLogs() {
    }

    @PrePersist
    public void init() {
        if(this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        created = LocalDateTime.now();
    }
}
