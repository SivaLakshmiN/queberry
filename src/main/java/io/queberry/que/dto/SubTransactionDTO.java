package io.queberry.que.dto;

import io.queberry.que.entity.SubTransaction;
import io.queberry.que.enums.InputType;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@ToString
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@Slf4j
public class SubTransactionDTO{
    private String id;
    private String name;
    private String displayName;
    private String secondaryName;
    private InputType inputType;
    private boolean active;
    private boolean input;
    private String selectionValues;

    public SubTransactionDTO(SubTransaction subTransaction){
        this.id = subTransaction.getId();
        this.name = subTransaction.getName();
        this.displayName = subTransaction.getDisplayName();
        this.secondaryName = subTransaction.getSecondaryName();
        this.inputType = subTransaction.getInputType();
        this.active = subTransaction.isActive();
        this.input = subTransaction.isInput();
        this.selectionValues = subTransaction.getSelectionValues();
    }
}

