package io.queberry.que.config;
import io.queberry.que.entity.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SurveyConfiguration extends AggregateRoot<SurveyConfiguration> {

    @Column(columnDefinition = "bit default 0")
    private boolean enabled;

    @Column(columnDefinition = "bit default 0")
    private Boolean sms=Boolean.FALSE;

    @Column(columnDefinition = "bit default 0")
    private Boolean email=Boolean.FALSE;

    @Column(columnDefinition = "bit default 0")
    private Boolean device=Boolean.FALSE;

    private Integer timeout;

    @Column(columnDefinition = "nvarchar(255)")
    private String message;

    @Column(name = "entity_trigger")
    @Enumerated(EnumType.STRING)
    private Trigger trigger;

    // Added fields for SMS
    @Column(columnDefinition = "nvarchar(255)")
    private String smsTextEnglish;

    @Column(columnDefinition = "nvarchar(255)")
    private String smsTextSecondary;

    private String emailContent;
    private String emailSubject;
    @Column(columnDefinition = "int default 0")
    private Integer retriggerDays=0;
    @Column(columnDefinition = "int default 3")
    private Integer expiryDays=3;

    private String surveyUrl;

    private String surveyKey;

    public SurveyConfiguration change(SurveyConfiguration surveyConfiguration){
        this.enabled = surveyConfiguration.isEnabled();

        if(surveyConfiguration.getSms() != null){
            this.sms = surveyConfiguration.getSms();
        }
        if(surveyConfiguration.getEmail() != null){
            this.email = surveyConfiguration.getEmail();
        }
        if(surveyConfiguration.getDevice() != null){
            this.device=surveyConfiguration.getDevice();
        }

        this.timeout = surveyConfiguration.getTimeout();
        this.message = surveyConfiguration.getMessage();
        this.trigger = surveyConfiguration.getTrigger();
        this.smsTextEnglish = surveyConfiguration.getSmsTextEnglish();
        this.smsTextSecondary = surveyConfiguration.getSmsTextSecondary();
        this.emailSubject = surveyConfiguration.getEmailSubject();
        this.emailContent = surveyConfiguration.getEmailSubject();
        this.retriggerDays = surveyConfiguration.getRetriggerDays();
        this.expiryDays = surveyConfiguration.getExpiryDays();
        this.surveyUrl = surveyConfiguration.getSurveyUrl();

        return this;
    }

    public enum Trigger{
        BEFORE,
        AFTER,
        NONE
    }
}
