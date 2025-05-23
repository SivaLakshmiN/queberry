package io.queberry.que.dto;
import io.queberry.que.entity.Branch;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@ToString
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@Slf4j
public class BranchEmployeeDTO {

    private String id;

    private String name;

    private boolean active;

    private String branchKey;

    public BranchEmployeeDTO(String id,
                             String name,
                             boolean active,
                             String branchKey) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.branchKey = branchKey;
    }

}
