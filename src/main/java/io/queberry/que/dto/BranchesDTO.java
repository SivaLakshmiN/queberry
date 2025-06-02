package io.queberry.que.dto;

import io.queberry.que.entity.Address;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.Set;

@Setter
@Getter
@ToString
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@Slf4j
public class BranchesDTO {

    private String id;

    private String name;

    private String branchKey;

    public BranchesDTO(String id, String name,String branchKey) {
        this.id = id;
        this.name = name;
        this.branchKey= branchKey;
    }
}
