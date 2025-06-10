package io.queberry.que.queue;

import io.queberry.que.entity.AggregateRoot;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "COUNTER_TOKEN")
@Table(name = "COUNTER_TOKEN", indexes = {
        @Index(name="idx_ct_token", columnList = "tokenId", unique = true),
        @Index(name="idx_ct_counter_createdAt", columnList = "counterId, createdAt"),
        @Index(name="idx_ct_employee_createdAt", columnList = "employeeId, createdAt")
})

@Getter
@Setter
@ToString
@NoArgsConstructor(force = true)
@EqualsAndHashCode
public class CounterToken extends AggregateRoot<CounterToken> {

    private String tokenId;

    private String counterId;

    private String employeeId;

    private LocalDateTime createdAt;

//    public static CounterToken of(Token token, Counter counter){
//        return new CounterToken(token.getId(),counter.getId(),token.getCreatedAt());
//    }
//
//
//    public CounterToken(String tokenId, String counterId, LocalDateTime createdAt) {
//        this.tokenId = tokenId;
//        this.counterId = counterId;
//        this.createdAt = createdAt;
//    }
//
//    public CounterToken(String tokenId, String employeeId, LocalDateTime createdAt, boolean isempId) {
//        if(isempId){
//            this.tokenId = tokenId;
//            this.employeeId = employeeId;
//            this.createdAt = createdAt;
//        }
//    }
}