package io.queberry.que.session;
import io.queberry.que.counter.Counter;
import io.queberry.que.employee.Employee;
import io.queberry.que.subTransaction.SubTransaction;
import io.queberry.que.entity.AggregateRoot;
import io.queberry.que.entity.Service;
import io.queberry.que.enums.Status;
import io.queberry.que.exception.QueueException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static io.queberry.que.enums.Status.*;


@Entity(name = "que_session")
@Getter
@ToString
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = true,of = "createdAt")
public class Session extends AggregateRoot<Session> {

    @ManyToOne
    private final Service service;

    @ManyToOne
    private SubTransaction subService;

    @ManyToOne
    private final Counter counter;

    private String employee;

    @Column(name = "entity_status")
    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime servingStart;

    private LocalDateTime completedAt;

    private Long serveTime = 0L;

    @ManyToOne
    private Service transferredToService;

    @ManyToOne
    private Counter transferredToCounter;

    @ManyToOne
    private Employee transferredToUser;

    private boolean ongoing;

    private String remarks;

    private String subServiceData;

    @Setter
    @Transient
    private Employee employeeMeta;

    public Session(Service service, Counter counter, String employee){
        this.service = service;
        this.counter = counter;
        this.employee = employee;
        this.status = ATTENDING;
        this.ongoing = true;
        this.servingStart = LocalDateTime.now();
    }

    public Session noShow(){
        this.status = Status.NO_SHOW;
        closure();
        return this;
    }

    public Session complete(SubTransaction subService, String remarks, String subServiceData){
        if (this.status != ATTENDING)
            throw new QueueException("Session not in attending state to complete",HttpStatus.PRECONDITION_FAILED);
        if(subService != null)
            this.subService =subService;
        this.status = COMPLETED;
        this.remarks = remarks;
        this.subServiceData = subServiceData;
        closure();
        return this;
    }

    public Long getServeTime() {
        if (serveTime == null || serveTime.equals(0L) || isOngoing())
            return Duration.between(servingStart,LocalDateTime.now()).getSeconds();
        return serveTime;
    }

    public Session hold(String remarks){
        this.status = Status.HOLD;
        this.remarks = remarks;
        closure();
        return this;
    }

    public Session transferToService(Service service){
        this.status = TRANSFERRED_TO_SERVICE;
        this.transferredToService = service;
        closure();
        return this;
    }

    public Session transferToUser(Employee employee){
        this.status = TRANSFERRED_TO_USER;
        this.transferredToUser = employee;
        closure();
        return this;
    }

    public Session transferToCounter(Counter counter){
        this.status = TRANSFERRED_TO_COUNTER;
        this.transferredToCounter = counter;
        closure();
        return this;
    }

    public void closure(){
        this.completedAt = LocalDateTime.now();
        this.ongoing = false;
        this.serveTime = Duration.between(servingStart,completedAt).getSeconds();
    }

    public Long getServeTimeInMins(){
        if (this.serveTime != null){
            return TimeUnit.SECONDS.toMinutes(serveTime);
        }
        return 0L;
    }
}


