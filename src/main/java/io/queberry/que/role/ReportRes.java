package io.queberry.que.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRes {
    private String id;
    private String name;
    private String region;
    private Report report;
}