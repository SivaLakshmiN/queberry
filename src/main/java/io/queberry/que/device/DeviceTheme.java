package io.queberry.que.device;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.queberry.que.entity.AuditedEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DeviceTheme extends AuditedEntity {

    private String signageLogo;

    private String dispenserLogo;

    private String printerLogo;

    private String primaryColor;

    private String secondaryColor;

    private String primaryTextColor;

    private String secondaryTextColor;

    private String backgroundImage;

    private boolean showTime;

    private boolean showArabic;

    private boolean showWaitingCustomers;

    private boolean showWaitingTime;

    public DeviceTheme change(DeviceTheme theme){
        this.signageLogo = theme.getSignageLogo();
        this.dispenserLogo = theme.getDispenserLogo();
        this.printerLogo = theme.getPrinterLogo();
        this.primaryColor = theme.getPrimaryColor();
        this.secondaryColor = theme.getSecondaryColor();
        this.primaryTextColor = theme.getPrimaryTextColor();
        this.secondaryTextColor = theme.getSecondaryTextColor();
        this.backgroundImage = theme.getBackgroundImage();
        this.showTime = theme.isShowTime();
        this.showArabic = theme.isShowArabic();
        this.showWaitingCustomers = theme.isShowWaitingCustomers();
        this.showWaitingTime = theme.isShowWaitingTime();
        return this;
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
