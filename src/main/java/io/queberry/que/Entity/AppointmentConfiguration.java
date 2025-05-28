package io.queberry.que.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.queberry.que.Controller.AppointmentConfigurationController;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Entity
@Getter
@ToString
@NoArgsConstructor(force = true,access = AccessLevel.PUBLIC)
public class AppointmentConfiguration extends AggregateRoot<AppointmentConfiguration> {


    private boolean enabled;
    private boolean sms;
    @Column(unique = true)
    private String branch;
    @Column(name = "sms_alert", nullable = false, columnDefinition = "bit default 1")
    private boolean smsAlert=Boolean.TRUE;
    @Column(name = "email_alert", nullable = false, columnDefinition = "bit default 0")
    private boolean emailAlert=Boolean.FALSE;
    @Column(name = "whatsapp_alert", nullable = false, columnDefinition = "bit default 0")
    private boolean whatsappAlert=Boolean.FALSE;
    @Column(name = "f2f_mode", nullable = false, columnDefinition = "bit default 1")
    private boolean f2fMode=Boolean.TRUE;
    @Column(name = "virtual_mode", nullable = false, columnDefinition = "bit default 0")
    private boolean virtualMode = Boolean.FALSE;
    @Column(name = "single", nullable = false, columnDefinition = "bit default 1")
    private boolean single=Boolean.TRUE;
    @Column(name = "bulk_app", nullable = false, columnDefinition = "bit default 0")
    private boolean bulkApp=Boolean.FALSE;
    // Added fields for SMS
    @Column(columnDefinition = "nvarchar(255)")
    private String smsTextEnglish;
    @Column(columnDefinition = "nvarchar(255)")
    private String smsTextSecondary;
    @Column(columnDefinition = "nvarchar(255)")
    private String apptEditedEnglish;
    @Column(columnDefinition = "nvarchar(255)")
    private String apptEditedSecondary;
    @Column(columnDefinition = "nvarchar(255)")
    private String apptCancelEnglish;
    @Column(columnDefinition = "nvarchar(255)")
    private String apptCancelSecondary;
    @Column(name = "check_in_time", nullable = false, columnDefinition = "int default 30")
    private int checkInTime;
    @Column(name = "check_in_max_time", nullable = false, columnDefinition = "int default 10")
    private int checkInMaxTime = 10;
    @Column(name = "max_slots", nullable = false, columnDefinition = "int default 1")
    private int maxSlots = 1;
    private String tenantId;
    private String clientId;
    private String clientSecret;
    private String userName;
    private String subject;
    @Column(columnDefinition = "nvarchar(255)")
    private String body;
    private String confirmedContent;
    private String confirmedSubject;
    private String cancelledContent;
    private String cancelledSubject;
    private String editedContent;
    private String editedSubject;
    private String confirmedVContent;
    private String confirmedVSubject;
    private String cancelledVContent;
    private String cancelledVSubject;
    private String editedVContent;
    private String editedVSubject;
    private String hostUrl;
    private String cancelReason;
    private String editReason;

    public AppointmentConfiguration change(AppointmentConfigurationController.AppointmentConfigurationResource resource){
//        if(resource.getBranch() != null)
//            this.branch = resource.getBranch().getBranchKey();
        if(resource.getEnabled() != null)
            this.enabled = resource.getEnabled();
//        this.sms = resource.isSms();
        if(resource.getSmsTextEnglish() != null)
            this.smsTextEnglish = resource.getSmsTextEnglish();
        if(resource.getSmsTextSecondary() != null)
            this.smsTextSecondary = resource.getSmsTextSecondary();
        if(resource.getCheckInTime() != null)
            this.checkInTime = Integer.parseInt(resource.getCheckInTime());
        if(resource.getCheckInMaxTime() != null)
            this.checkInMaxTime = Integer.parseInt(resource.getCheckInMaxTime());

        if(resource.getClientSecret() != null)
            this.clientSecret = resource.getClientSecret();
        if(resource.getClientId() != null)
            this.clientId = resource.getClientId();
        if(resource.getTenantId() != null)
            this.tenantId = resource.getTenantId();
        if(resource.getUserName() != null)
            this.userName = resource.getUserName();
        if(resource.getSubject() != null)
            this.subject = resource.getSubject();
        if(resource.getBody()!=null)
            this.body = resource.getBody();

        if(resource.getConfirmedVContent() != null)
            this.confirmedVContent = resource.getConfirmedVContent();
        if(resource.getConfirmedVSubject() != null)
            this.confirmedVSubject = resource.getConfirmedVSubject();
        if(resource.getEditedVContent() != null)
            this.editedVContent = resource.getEditedVContent();
        if(resource.getEditedVSubject() != null)
            this.editedVSubject = resource.getEditedVSubject();
        if(resource.getCancelledVContent() != null)
            this.cancelledVContent = resource.getCancelledVContent();
        if(resource.getCancelledVSubject() != null)
            this.cancelledVSubject = resource.getCancelledVSubject();

        if(resource.getApptCancelEnglish() != null)
            this.apptCancelEnglish = resource.getApptCancelEnglish();
        if(resource.getApptEditedEnglish() != null)
            this.apptEditedEnglish = resource.getApptEditedEnglish();
        if(resource.getCancelReason() != null)
            this.cancelReason = resource.getCancelReason();
        if(resource.getEditReason() != null)
            this.editReason = resource.getEditReason();

        if(resource.getSms_alert() != null)
            this.smsAlert = resource.getSms_alert();
        if(resource.getEmail_alert() != null)
            this.emailAlert = resource.getEmail_alert();
        if(resource.getWhatsapp_alert() != null)
            this.whatsappAlert = resource.getWhatsapp_alert();
        if(resource.getF2f_mode() != null)
            this.f2fMode = resource.getF2f_mode();
        if(resource.getVirtual_mode() != null)
            this.virtualMode = resource.getVirtual_mode();
        if(resource.getSingle() != null)
            this.single = resource.getSingle();
        if(resource.getBulk() != null)
            this.bulkApp = resource.getBulk();

        if(resource.getMaxSlots() != null)
            this.maxSlots = Integer.parseInt(resource.getMaxSlots());
        if(resource.getConfirmedContent() != null)
            this.confirmedContent = resource.getConfirmedContent();
        if(resource.getConfirmedSubject() != null)
            this.confirmedSubject = resource.getConfirmedSubject();
        if(resource.getEditedContent() != null)
            this.editedContent = resource.getEditedContent();
        if(resource.getEditedSubject() != null)
            this.editedSubject = resource.getEditedSubject();
        if(resource.getCancelledContent() != null)
            this.cancelledContent = resource.getCancelledContent();
        if(resource.getCancelledSubject() != null)
            this.cancelledSubject = resource.getCancelledSubject();

        if(resource.getHostUrl() != null)
            this.hostUrl = resource.getHostUrl();

        return this;
    }


    public AppointmentConfiguration(boolean enabled, boolean sms, String smsTextEnglish, String smsTextSecondary, String apptEditedEnglish, String apptCancelEnglish, int checkInTime, String tenantId, String clientId, String clientSecret, String userName, String subject, String body) {
        this.enabled = enabled;
        this.sms = sms;
        this.smsTextEnglish = smsTextEnglish;
        this.smsTextSecondary = smsTextSecondary;
        this.checkInTime = checkInTime;
        this.userName = userName;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tenantId = tenantId;
        this.subject = subject;
        this.body = body;
        this.apptEditedEnglish = apptEditedEnglish;
        this.apptCancelEnglish = apptCancelEnglish;
    }

    @Override
    @JsonIgnore
    public String getId(){
        return super.getId();
    }

    @Override
    @JsonIgnore
    public Long getVersion(){
        return super.getVersion();
    }
}
