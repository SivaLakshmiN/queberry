package io.queberry.que.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "SubTransactionCache")
public class SubTransaction extends AggregateRoot<SubTransaction> {

    @Column(columnDefinition = "bit default 1")
    private boolean active;

    @Column(unique = true,columnDefinition = "nvarchar(200)",nullable = false)
    private String name;

    @Column(columnDefinition = "nvarchar(200)",nullable = false)
    private String displayName;

    @Column(columnDefinition = "nvarchar(200)",nullable = false)
    private String secondaryName;

    @Column(columnDefinition = "bit default 1")
    private boolean input;

    //private InputType inputType;

    private String selectionValues;

    @ManyToOne
    private Region region;

}

