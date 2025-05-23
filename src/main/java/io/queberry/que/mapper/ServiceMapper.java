package io.queberry.que.mapper;

import io.queberry.que.entity.Service;
import io.queberry.que.entity.SharedSequence;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor(force = true)
@EqualsAndHashCode
public class ServiceMapper {
    private String id;
    private String name;
    private Integer sequenceStart;
    private Integer sequenceEnd;
    private String sharedSequence;

    ServiceMapper(Service service){
        this.id = service.getId();
        this.name = service.getName();
        this.sequenceStart = service.getSequenceStart();
        this.sequenceEnd = service.getSequenceEnd();
        this.sharedSequence = service.getSharedSequence();
    }
}

