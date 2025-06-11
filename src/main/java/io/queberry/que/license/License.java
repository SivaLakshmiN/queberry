package io.queberry.que.license;

import io.queberry.que.entity.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import java.time.LocalDate;


//@Data

@NoArgsConstructor(force = true)
//@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Table
//@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor
@Entity(name = "license")
public class License extends AggregateRoot<License> {
    private String orgName; // package

    private LocalDate startDate; //package

    private LocalDate endDate; //package

    private int counters = 0;

    private int signages = 0;

    private int dispensers = 0;

    private int tokensPerDay = 0;

    private int users = 0;

    private int branches = 0;

    @Column(unique = true)
    private String package_name;

    @Column(columnDefinition = "bit default 1")
    private boolean m_qms;
    @Column(columnDefinition = "bit default 1")
    private boolean m_signage;
    @Column(columnDefinition = "bit default 1")
    private boolean m_business_analysis;
    @Column(columnDefinition = "bit default 1")
    private boolean m_alerts;
    @Column(columnDefinition = "bit default 1")
    private boolean m_survey;
    @Column(columnDefinition = "bit default 0")
    private boolean m_appointment;
    @Column(columnDefinition = "bit default 1")
    private boolean basicReports;
    @Column(columnDefinition = "bit default 0")
    private boolean powerBI;
    @Column(columnDefinition = "bit default 0")
    private boolean smsAlerts;
    @Column(columnDefinition = "bit default 0")
    private boolean emailAlerts;
    @Column(columnDefinition = "bit default 0")
    private boolean surveyTab;
    @Column(columnDefinition = "bit default 0")
    private boolean surveySms;
    @Column(columnDefinition = "bit default 0")
    private boolean virtualAppointment;
    @Column(columnDefinition = "bit default 0")
    private boolean faceToFace; // faceToFace
    @Column(columnDefinition = "bit default 1")
    private boolean enable;
}

