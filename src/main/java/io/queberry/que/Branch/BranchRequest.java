package io.queberry.que.Branch;

import io.queberry.que.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchRequest {
    private String name;
    private Address address;
    private LocalTime businessStart;
    private LocalTime businessEnd;
    private String region;
    private boolean globalServices;
    private Set<String> services;
    private Set<String> serviceGroup;
    private String branchKey;
    private boolean isGlobalAppointment;
    private String comPort;
    private int capacity;
}
