package io.queberry.que.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DispenserConfiguration extends AggregateRoot<DispenserConfiguration> {

    private String welcomeMessage;

    private Integer numberOfTokens;

    private boolean showClock;

    private String messageAfterBusinessHours;

    private boolean blurBackground;

    private boolean enableServiceGroup;

    private boolean showBarcode;

    @Column(unique = true)
    private String branchKey;

    private boolean showArabic;

    private boolean showWaitingCustomers;

    private boolean showWaitingTime;

    private boolean showInQueueAlert;
    @Column(name = "eid_scan", nullable = false, columnDefinition = "bit default 0")
    private boolean eidScan;

    @Column(columnDefinition = "varchar(2000)")
    private String languages;

    public List<Language> getLanguageList() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(this.languages,
                    mapper.getTypeFactory().constructCollectionType(List.class, Language.class));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public void setLanguageList(List<Language> languageList) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.languages = mapper.writeValueAsString(languageList);
        } catch (JsonProcessingException e) {
            this.languages = "[]";
        }
    }

    //    private boolean sendInQueueMeetMail; not yet added

//    public DispenserConfiguration change(DispenserConfigurationController.DispenserConfigurationResource resource){
//        this.welcomeMessage = resource.getWelcomeMessage();
//        this.numberOfTokens = resource.getNumberOfTokens();
//        this.showClock = resource.isShowClock();
//        this.messageAfterBusinessHours = resource.getMessageAfterBusinessHours();
//        this.blurBackground = resource.isBlurBackground();
//        this.enableServiceGroup = resource.isEnableServiceGroup();
//        this.showBarcode = resource.isShowBarcode();
//        this.showArabic = resource.isShowArabic();
//        this.showWaitingCustomers = resource.isShowWaitingCustomers();
//        this.showWaitingTime = resource.isShowWaitingTime();
//        this.showInQueueAlert = resource.isShowInQueueAlert();
//        this.eidScan = resource.isEidScan();
//        //this.languages=resource.getLanguages();
//        setLanguageList(resource.getLanguages());
//        return andEvent(ConfigurationEvent.of(this));
//    }

    public DispenserConfiguration(String welcomeMessage, Integer numberOfTokens, boolean showClock,
                                  String messageAfterBusinessHours, boolean blurBackground,
                                  boolean enableServiceGroup, boolean showBarcode, String branchKey,
                                  boolean showArabic, boolean showWaitingCustomers, boolean showWaitingTime,
                                  boolean showInQueueAlert, boolean eidScan){
        welcomeMessage = "Welcome";
        numberOfTokens = 1;
        showClock = true;
        messageAfterBusinessHours = "We are closed";
        blurBackground = false;
        enableServiceGroup = false;
        showBarcode = false;
        showArabic = false;
        showWaitingCustomers = false;
        showWaitingTime = false;
        showInQueueAlert = false;
        eidScan = false;
        languages = "[]";
        registerEvent(ConfigurationEvent.of(this));
    }

    public DispenserConfiguration(String key){
        welcomeMessage = "Welcome";
        numberOfTokens = 1;
        showClock = true;
        branchKey = key;
        messageAfterBusinessHours = "We are closed";
        blurBackground = false;
        enableServiceGroup = false;
        showBarcode = false;
        showArabic = false;
        showWaitingCustomers = false;
        showWaitingTime = false;
        showInQueueAlert = false;
        eidScan = false;
        languages = "[]";
        registerEvent(ConfigurationEvent.of(this));
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
