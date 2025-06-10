package io.queberry.que.queue;

import io.queberry.que.entity.AggregateRoot;
import io.queberry.que.service.Service;
import io.queberry.que.token.Token;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity(name = "APPOINTMENT_TOKEN")
@Getter
@ToString
@NoArgsConstructor(force = true)
@EqualsAndHashCode
public class AppointmentToken extends AggregateRoot<AppointmentToken> {

    private String tokenId;

    private String serviceId;

    private LocalDateTime appointmentAt;

    public static AppointmentToken of(Token token){
        return new AppointmentToken(token.getId(),token.getService().getId(),token.getCreatedAt());
    }

    public static AppointmentToken of(Token token, Service service){
        return new AppointmentToken(token.getId(),service.getId(),token.getCreatedAt());
    }

    public AppointmentToken(String tokenId, String serviceId, LocalDateTime appointmentAt) {
        this.tokenId = tokenId;
        this.serviceId = serviceId;
        this.appointmentAt = appointmentAt;
    }
}