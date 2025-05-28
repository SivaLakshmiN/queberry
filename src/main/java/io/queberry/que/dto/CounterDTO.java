package io.queberry.que.dto;

import io.queberry.que.entity.Counter;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Setter
@Getter
@ToString
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@Slf4j
public class CounterDTO {

    private String id;
    private String name;
    private String displayName;
    private String description;
    private String code;
    private boolean active;
    private Counter.Type type;
    private boolean inUse;
    private String colorCode;
    private String panelNumber;
    private String presentation;
    private Set<String> firstLevelServicesMeta;
    private Set<String> secondLevelServicesMeta;
    private Set<String> thirdLevelServicesMeta;
    private Set<String> fourthLevelServicesMeta;
}
