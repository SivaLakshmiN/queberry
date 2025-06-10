package io.queberry.que.ticker;

import io.queberry.que.entity.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;


@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Ticker extends AggregateRoot<Ticker> {

    @Column(columnDefinition = "nvarchar(255)")
    private String message;

    @Column(columnDefinition = "nvarchar(255)")
    private String language;
}
