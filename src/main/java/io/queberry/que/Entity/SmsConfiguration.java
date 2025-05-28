package io.queberry.que.Entity;
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
public class SmsConfiguration extends AggregateRoot<SmsConfiguration> {


    private String url;
    private String username;
    private String password;
    private String senderId;

    @Column(name = "SMS_UPDATES", columnDefinition = "bit default 0")
    private Boolean updates;

    @Column(name = "enabled", columnDefinition = "bit default 0")
    private boolean enabled;

    private Integer queueCount;

    @Column(name = "CHOOSE_MEDIUM", columnDefinition = "bit default 0")
    private Boolean chooseMedium;

    @Column(name = "SMS_CALL", columnDefinition = "bit default 0")
    private Boolean call;

    @Column(name = "SMS_TRANSFER", columnDefinition = "bit default 0")
    private Boolean transfer;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(columnDefinition = "nvarchar(255)")
    private String smsTextEnglish;

    @Column(columnDefinition = "nvarchar(255)")
    private String smsTextSecondary;

    @Column(columnDefinition = "nvarchar(255)")
    private String smsCalledTextEnglish;

    @Column(columnDefinition = "nvarchar(255)")
    private String smsCalledTextSecondary;

    @Column(columnDefinition = "nvarchar(255)")
    private String smsTransferCounterTextEnglish;

    @Column(columnDefinition = "nvarchar(255)")
    private String smsTransferCounterTextSecondary;

    @Column(columnDefinition = "nvarchar(255)")
    private String smsAlertTextEnglish;

    @Column(columnDefinition = "nvarchar(255)")
    private String smsAlertTextSecondary;

    @Column(columnDefinition = "nvarchar(255)")
    private String smsOTPTextEnglish;

    @Column(columnDefinition = "nvarchar(255)")
    private String smsOTPTextSecondary;

    public enum Provider{
        AJMAN_BANK_LOCAL,
        VECTRAMIMND,
        ETISALAT,
        SMS_COUNTRY,
        ETISALAT_REST,
        DP_REST,
        EMARAT,
        RTA
    }

    public SmsConfiguration change(SmsConfiguration smsConfiguration){
        this.enabled = smsConfiguration.isEnabled();
        if(smsConfiguration.getUrl() != null)
            this.url = smsConfiguration.getUrl();
        if(smsConfiguration.getUsername() != null)
            this.username = smsConfiguration.getUsername();
        if(smsConfiguration.getPassword() != null)
            this.password = smsConfiguration.getPassword();
        if(smsConfiguration.getProvider() != null)
            this.provider = smsConfiguration.getProvider();
        if(smsConfiguration.getSenderId() != null)
            this.senderId = smsConfiguration.getSenderId();
        if(smsConfiguration.getUpdates() != null)
            this.updates = smsConfiguration.getUpdates();
        if(smsConfiguration.getQueueCount() != null)
            this.queueCount = smsConfiguration.getQueueCount();
        if(smsConfiguration.getChooseMedium() != null)
            this.chooseMedium = smsConfiguration.getChooseMedium();
        if(smsConfiguration.getSmsTextEnglish() != null)
            this.smsTextEnglish = smsConfiguration.getSmsTextEnglish();
        if(smsConfiguration.getSmsTextSecondary() != null)
            this.smsTextSecondary = smsConfiguration.getSmsTextSecondary();
        if(smsConfiguration.getSmsCalledTextEnglish() != null)
            this.smsCalledTextEnglish = smsConfiguration.getSmsCalledTextEnglish();
        if(smsConfiguration.getSmsTextSecondary() != null)
            this.smsCalledTextSecondary = smsConfiguration.getSmsCalledTextSecondary();
        if(smsConfiguration.getSmsAlertTextEnglish() != null)
            this.smsAlertTextEnglish = smsConfiguration.getSmsAlertTextEnglish();
        if(smsConfiguration.getSmsAlertTextSecondary() != null)
            this.smsAlertTextSecondary = smsConfiguration.getSmsAlertTextSecondary();
        if(smsConfiguration.getSmsTransferCounterTextEnglish() != null)
            this.smsTransferCounterTextEnglish = smsConfiguration.getSmsTransferCounterTextEnglish();
        if(smsConfiguration.getSmsTransferCounterTextSecondary() != null)
            this.smsTransferCounterTextSecondary = smsConfiguration.getSmsTransferCounterTextSecondary();
        if(smsConfiguration.getCall()    != null)
            this.call = smsConfiguration.getCall();
        if(smsConfiguration.getTransfer() != null)
            this.transfer = smsConfiguration.getTransfer();
        if(smsConfiguration.getSmsOTPTextEnglish()!= null)
            this.smsOTPTextEnglish = smsConfiguration.getSmsOTPTextEnglish();
        if(smsConfiguration.getSmsOTPTextSecondary()!= null)
            this.smsOTPTextSecondary = smsConfiguration.getSmsOTPTextSecondary();
        return this;
    }
}
