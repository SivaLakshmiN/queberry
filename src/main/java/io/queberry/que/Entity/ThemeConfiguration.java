package io.queberry.que.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.queberry.que.Controller.ThemeConfigurationController;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ThemeConfiguration extends AggregateRoot<ThemeConfiguration> {


    private String signageLogo;

    private String dispenserLogo;

    private String printerLogo;

    private String feedbackLogo;

    private String qrcodeLogo;

    private String primaryColor;

    private String secondaryColor;

    private String primaryTextColor;

    private String secondaryTextColor;

    private String qrPrimaryColor;
    private String qrSecondaryColor;

    private String backgroundImage;

    private boolean showTime;

    private boolean showArabic;

    private boolean showWaitingCustomers;

    private boolean showWaitingTime;

    private boolean showInQueueAlert;

    private boolean sendInQueueMeetMail;

    private String loginScreenLogo;

    @Column(unique = true)
    private String branchKey;

    public ThemeConfiguration change(ThemeConfigurationController.Theme theme){
        if(theme.getSignageLogo() != null)
            this.signageLogo = theme.getSignageLogo();
        if(theme.getDispenserLogo() != null)
            this.dispenserLogo = theme.getDispenserLogo();
        if(theme.getPrinterLogo() != null)
            this.printerLogo = theme.getPrinterLogo();
        if(theme.getFeedbackLogo() != null)
            this.feedbackLogo = theme.getFeedbackLogo();
        if(theme.getPrimaryColor() != null)
            this.primaryColor = theme.getPrimaryColor();
        if(theme.getSecondaryColor() != null)
            this.secondaryColor = theme.getSecondaryColor();
        if(theme.getPrimaryTextColor() != null)
            this.primaryTextColor = theme.getPrimaryTextColor();
        if(theme.getSecondaryTextColor() != null)
            this.secondaryTextColor = theme.getSecondaryTextColor();
        if(theme.getBackgroundImage() != null)
            this.backgroundImage = theme.getBackgroundImage();
        return andEvent(ConfigurationEvent.of(this));
    }

    public ThemeConfiguration(String branchKey, String signageLogo, String dispenserLogo, String printerLogo, String feedbackLogo, String backgroundImage,
                              String loginScreenLogo, String primaryColor, String secondaryColor, String primaryTextColor, String secondaryTextColor){
        this.signageLogo = signageLogo;
        this.dispenserLogo = dispenserLogo;
        this.printerLogo = printerLogo;
        this.feedbackLogo = feedbackLogo;
        this.loginScreenLogo = loginScreenLogo;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.primaryTextColor = primaryTextColor;
        this.secondaryTextColor = secondaryTextColor;
        this.backgroundImage = backgroundImage;
        this.branchKey = branchKey;
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
