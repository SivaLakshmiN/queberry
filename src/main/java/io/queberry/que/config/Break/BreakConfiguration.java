package io.queberry.que.config.Break;
import io.queberry.que.entity.AggregateRoot;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(force = true,access = AccessLevel.PUBLIC)
public class BreakConfiguration extends AggregateRoot<BreakConfiguration> {

    private String breakName;

    private String breakDescription;

    private String duration;

    private boolean ifExceeds;

    private String approvalRequest;

}
