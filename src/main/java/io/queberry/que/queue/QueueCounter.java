package io.queberry.que.queue;

import io.queberry.que.entity.AggregateRoot;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;

@Entity(name = "QUEUED_COUNTER")
@Table(name = "QUEUED_COUNTER", indexes = {
        @Index(name="idx_qc_counterId", columnList = "counterId", unique = true),
        @Index(name="idx_qc_branch_createdAt", columnList = "branch, createdAt"),
        @Index(name="idx_qc_employee", columnList = "employee")
})
@Getter
@ToString
@NoArgsConstructor(force = true)
@AllArgsConstructor
@EqualsAndHashCode
public class QueueCounter extends AggregateRoot<QueueCounter> {

    private String counterId;

//    private String serviceId;

    private Long createdAt;

    private String employee;
    private String branch;

//    public static QueueCounter of(Counter counter){
//        return new QueueCounter(counter.getId(),counter.getCreatedAt());
//    }

//    public static QueueCounter of(Token token, Service service){
//        return new QueueCounter(token.getId(),token.getCreatedAt());
//    }

//    public QueueCounter(String counterId, Long createdAt) {
//        this.counterId = counterId;
////        this.serviceId = serviceId;
//        this.createdAt = createdAt;
//    }
}
