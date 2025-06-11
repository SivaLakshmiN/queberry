package io.queberry.que.branch;

import io.queberry.que.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchData {
    private String id;
    private String name;
    private String branchKey;
    private Address address;
    private LocalTime businessStart;
    private LocalTime businessEnd;
}
