package io.queberry.que.branch;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@Slf4j
public class BranchDTO {

    private String id;
    private String name;
//    private boolean active;
    private String branchKey;

    public BranchDTO() {
    }

//    public BranchDTO(String id, String name, boolean active, String branchKey) {
//        this.id = id;
//        this.name = name;
//        this.active = active;
//        this.branchKey = branchKey;
//    }

    public BranchDTO(String id, String name, String branchKey) {
        this.id = id;
        this.name = name;
        this.branchKey = branchKey;
    }
}
