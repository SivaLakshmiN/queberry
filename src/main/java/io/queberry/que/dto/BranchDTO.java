package io.queberry.que.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@ToString
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@Slf4j
public class BranchDTO {
    private String id;
    private String name;
    private boolean active;
    private String branchKey;
}
