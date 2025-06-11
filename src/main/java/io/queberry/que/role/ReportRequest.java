package io.queberry.que.role;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.queberry.que.branch.Branch;
import io.queberry.que.enums.Type;
import io.queberry.que.region.Region;
import io.queberry.que.service.Service;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportRequest {
    private LocalDate from;
    private LocalDate to;
    private Type type;
    private boolean aggregate;
    private Parties parties;
    private Set<String> serviceIds;
    private Set<Service> services;
    private Set<String> ids;
    private String empId;
    private Branch branch;
    private String region;
    private boolean filter;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Parties{
        private Set<String> ids = new HashSet<>(0);
        private boolean all;
    }
}
