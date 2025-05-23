package io.queberry.que.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public  class SubTransactionGroupReq {
    private Set<String> subTransactions;
    private boolean active;
    private String name;
    private String displayName;
    private String secondaryName;
    private String region;

}