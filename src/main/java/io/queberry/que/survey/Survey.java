package io.queberry.que.survey;

import io.queberry.que.entity.AggregateRoot;
import io.queberry.que.entity.DomainEvent;
import io.queberry.que.enums.Status;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Survey extends AggregateRoot<Survey> {

    private String title;

    private String fileName;

    private Status status;

    private int responseCount = 0;

    private LocalDateTime created;

    public Survey(String title, String fileName, Status status){
        this.title = title;
        this.fileName = fileName;
        this.status = status;
        this.created = LocalDateTime.now();
        this.registerEvent(SurveyAdded.of(this));
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class SurveyAdded extends DomainEvent<Survey> {
        Survey survey;
    }

}

