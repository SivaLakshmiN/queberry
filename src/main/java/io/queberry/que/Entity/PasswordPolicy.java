package io.queberry.que.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PasswordPolicy extends AggregateRoot<PasswordPolicy> {

    @Column(columnDefinition = "integer default 3")
    private Integer failedAttempts;

    @Column(columnDefinition = "integer default 8")
    private Integer length;

    @Column(columnDefinition = "integer default 90")
    private Integer expireDays;

    @Enumerated(EnumType.STRING)
    private Chanel chanel;

    @Enumerated(EnumType.STRING)
    private ResetMode resetMode;
}
