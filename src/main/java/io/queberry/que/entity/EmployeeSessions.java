package io.queberry.que.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name="que_employee_session")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "que_employee_session", indexes = {@Index(name="idx_emp", columnList="loginTime, employee_id")})
public class EmployeeSessions extends AggregateRoot<EmployeeSessions> {


    @ManyToOne
//    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LocalDateTime loginTime;

    private LocalDateTime logoutTime;

    public EmployeeSessions(Employee e, LocalDateTime loginTime){
        this.employee = e;
        this.loginTime = loginTime;
    }



}

