package io.queberry.que.mapper;

import io.queberry.que.service.Service;
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

