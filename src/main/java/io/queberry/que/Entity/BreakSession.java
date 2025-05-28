package io.queberry.que.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BreakSession extends AggregateRoot<BreakSession> {

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "break_configuration_id")
    private BreakConfiguration breakConfiguration;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;


}
