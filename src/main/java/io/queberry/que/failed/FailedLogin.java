package io.queberry.que.failed;

import io.queberry.que.entity.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "failed_logins")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FailedLogin extends AggregateRoot<FailedLogin> {

    private String employeeId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime attemptTime = LocalDateTime.now();

    // Getters and Setters
}
