package io.queberry.que.dto;

import io.queberry.que.entity.SubTransactionGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubTransactionGroupDTO {
    private String id;
    private String name;
    private String displayName;
    private String secondaryName;
    private boolean active;
    private Set<String> subTransactions;

    public SubTransactionGroupDTO(SubTransactionGroup subTransactionGroup){
        this.id = subTransactionGroup.getId();
        this.name = subTransactionGroup.getName();
        this.displayName = subTransactionGroup.getDisplayName();
        this.secondaryName = subTransactionGroup.getSecondaryName();
        this.active = subTransactionGroup.isActive();
//        this.subTransactions =subTransactionGroup.getSubTransactions();
    }

    private Set<String> getSubTransactionDTO(Set<String> subTransaction){
        return subTransaction.stream().map(String::new).collect(Collectors.toSet());
    }
}


