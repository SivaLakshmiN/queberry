package io.queberry.que.sharedSequence;

import io.queberry.que.region.Region;
import io.queberry.que.entity.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "shared_sequence")
@Getter
@Setter
@ToString(of = "name")
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "SharedSequenceCache")
public class SharedSequence extends AggregateRoot<SharedSequence> {

    @Column(unique = true, nullable = false)
    private String name;

    private String displayName;

    private String code;

    @Column(columnDefinition = "bit default 0")
    private boolean serviceCode;

    private String seperator; // `separator is a keyword in mysql`

    private Integer sequenceStart;

    private Integer sequenceEnd;

    @Column(columnDefinition = "bit default 0")
    private boolean format;  // to format the token to desired length ex: 0001 use %04d, 001 use %03d

    @Column(columnDefinition = "bit default 1")
    private boolean active;

    @ManyToOne
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "RegionCache")
    private Region region;
}
