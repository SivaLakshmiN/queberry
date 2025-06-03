package io.queberry.que.sharedSequence;

import io.queberry.que.mapper.ServiceMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
@NoArgsConstructor(force = true,access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class SharedSequenceManager {

    private String sharedSequence;

    @Setter
    private Integer sequenceStart;

    private Integer sequenceEnd;

    public SharedSequenceManager(ServiceMapper service) {
        this.sharedSequence = service.getSharedSequence();
        this.sequenceStart = service.getSequenceStart();
        this.sequenceEnd = service.getSequenceEnd();
    }

    public Integer getNext(){
        Integer seq = sequenceStart;
        if (seq.equals(sequenceEnd)){
            seq = sequenceStart;
            sequenceStart = Integer.valueOf(sharedSequence);
        }
        sequenceStart++;
        return seq;
    }

    public SharedSequenceManager reset(){
        this.sequenceStart = Integer.valueOf(sharedSequence);
        return this;
    }
}
