package io.queberry.que.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchDTO {
    private String id;
    private String name;
    private String branchKey;
}


