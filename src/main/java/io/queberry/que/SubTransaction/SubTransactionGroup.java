package io.queberry.que.SubTransaction;

import io.queberry.que.entity.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jetbrains.annotations.NotNull;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "SubTrxnGroupCache")
public class SubTransactionGroup extends AggregateRoot<SubTransactionGroup> {

    @NotNull()
    @Column(unique = true,columnDefinition = "nvarchar(200)",nullable = false)
    private String name;

    @NotNull()
    @Column(columnDefinition = "nvarchar(200)",nullable = false)
    private String displayName;

    @Column(columnDefinition = "bit default 1")
    private boolean active = true;

    @Column(columnDefinition = "nvarchar(200)",nullable = false)
    private String secondaryName;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @NotNull( )
//    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "SubTransactionCache")
//    private Set<String> subTransactions = new HashSet<>(0);

    //    @Column(name = "region_id")
    private String region;
}


