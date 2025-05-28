package io.queberry.que.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "SubTrxnGroupCache")
public class SubTransactionGroup extends AggregateRoot<SubTransactionGroup> {

   //@NotNull(message = "SubService Group name is required!!")
    @Column(unique = true,columnDefinition = "nvarchar(200)",nullable = false)
    private String name;

    //@NotNull(message = "SubService Group display name is required!!")
    @Column(columnDefinition = "nvarchar(200)",nullable = false)
    private String displayName;

    @Column(columnDefinition = "bit default 1")
    private boolean active = true;

    @Column(columnDefinition = "nvarchar(200)",nullable = false)
    private String secondaryName;

    @ManyToMany(fetch = FetchType.EAGER)
    //@NotNull(message = "SubTransaction to be added SubTransaction Group is required!!")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "SubTransactionCache")
    private Set<SubTransaction> subTransactions = new HashSet<>(0);

    @ManyToOne
    private Region region;

}

