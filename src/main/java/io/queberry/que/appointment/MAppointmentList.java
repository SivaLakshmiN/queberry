package io.queberry.que.appointment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.queberry.que.entity.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MAppointmentList extends AggregateRoot<MAppointmentList> {

    // @Setter
    private String branch;
    private String service;

    private String serviceid;

    private LocalDateTime date;

    @Column(name = "_FROM")
    private LocalTime from;

    @Column(name = "_TO")
    private LocalTime to;

    private String name;

    private String email;

    private String mobile;

    private String smsOtp;

    private String checkinCode;

    private LocalDateTime checkedInAt;

    //    @Enumerated(EnumType.STRING)
    private String state;

    private boolean isDelete = Boolean.FALSE;

    @JsonIgnore
    //@Setter
//    @Enumerated(EnumType.STRING)
    private String appointmentType;

    private String teamsId;

    //    @Enumerated(EnumType.STRING)
    private String typeofAppointment;

    private String tenant;

//    public MAppointmentList(Service service, LocalDate date, String name, String email, String mobile){
//        //this.branch = branch;
//        //super(branch);
//        this.service = service;
//        this.date = date;
//        this.name = name;
//        this.email = email;
//        this.mobile = mobile;
//        this.state = State.CONFIRMED;
//        this.typeofAppointment = TypeofAppointment.WALKIN;
//        this.isDelete = Boolean.FALSE;
//    }


//    public enum State{
//        PENDING_OTP_VERIFICATION,
//        CONFIRMED,
//        CHECKEDIN,
//        WALKIN,
//        NOSHOW,
//        COMPLETED,
//        CANCELLED,
//        EXPIRED
//    }
//
//    public enum AppointmentType{
//        NEW,
//        EDIT
//    }
//
//    public enum TypeofAppointment{
//        F2F,
//        VIRTUAL,
//        WALKIN
//    }
}
