package io.queberry.que.queue;


import io.queberry.que.entity.AggregateRoot;
import io.queberry.que.service.Service;
import io.queberry.que.token.Token;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity(name = "QUEUED_TOKEN")
@Table(name = "QUEUED_TOKEN", indexes = {@Index(name="idx_qt_branch",columnList = "branch"),
        @Index(name="idx_qt_token", columnList = "tokenId"),
        @Index(name="idx_qt_branch_service_priority_createdat", columnList = "branch, serviceId, priority, createdAt")})
@Getter
@ToString
@NoArgsConstructor(force = true)
@EqualsAndHashCode
public class QueueToken extends AggregateRoot<QueueToken> {

    private String tokenId;

    private String serviceId;

    private LocalDateTime createdAt;

    private String branch;

    private Integer priority;

    public static QueueToken of(Token token) {
        return new QueueToken(token.getId(), token.getService().getId(), token.getCreatedAt(), token.getBranch(), token.getPriority());
    }

    public static QueueToken of(Token token, Service service) {
        return new QueueToken(token.getId(), service.getId(), token.getCreatedAt(), token.getBranch(), token.getPriority());
    }

//    public static QueueToken of(Token token, Employee employee){
//        return new QueueToken(token.getId(),token.getService().getId(),token.getCreatedAt(), token.getBranch());
//    }

    public QueueToken(String tokenId, String serviceId, LocalDateTime createdAt, String branchKey, Integer priority) {
        this.tokenId = tokenId;
        this.serviceId = serviceId;
        this.createdAt = createdAt;
        this.branch = branchKey;
        this.priority = priority;
    }
}
