package io.queberry.que.SharedSequence;
import io.queberry.que.mapper.ServiceMapper;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(force = true,access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class SequenceManager {

    private ServiceMapper service;

    @Setter
    private Integer sequenceStart;

    private Integer sequenceEnd;

    public SequenceManager(ServiceMapper service) {
        this.service = service;
        this.sequenceStart = service.getSequenceStart();
        this.sequenceEnd = service.getSequenceEnd();
    }

    public Integer getNext(){
        Integer seq = sequenceStart;
        if (seq.equals(sequenceEnd)){
            seq = sequenceStart;
            sequenceStart = service.getSequenceStart();
        }
        sequenceStart++;
        return seq;
    }

    public SequenceManager reset(){
        this.sequenceStart = service.getSequenceStart();
        return this;
    }
}
