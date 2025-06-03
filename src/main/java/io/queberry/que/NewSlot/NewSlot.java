package io.queberry.que.NewSlot;

import io.queberry.que.Region.Region;
import io.queberry.que.entity.AggregateRoot;
import io.queberry.que.entity.Service;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(indexes = {@Index(name="nsbr_index",columnList = "branch")})
@NoArgsConstructor
@AllArgsConstructor
public class NewSlot extends AggregateRoot<NewSlot> {

    @ManyToOne
    private Region region;

    private String branch;

    @ManyToOne
    private Service service;

    private LocalDate fromDate;

    private LocalDate toDate;

    private LocalTime startTime;   // start time and end time can be equal to business time

    private LocalTime endTime;

    private int duration;

    private int totalCapacity = 0;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Day> weekdays;

    public enum Day{
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY
    }
}
