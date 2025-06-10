package io.queberry.que.counter;

import io.queberry.que.entity.AggregateRoot;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CounterStatus extends AggregateRoot<CounterStatus> {
    private String counter;
    private String status;
}
