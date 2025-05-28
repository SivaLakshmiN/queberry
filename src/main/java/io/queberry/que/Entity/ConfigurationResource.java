package io.queberry.que.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@Slf4j
public class ConfigurationResource {

    private final AudioConfiguration audio;

    private final DispenserConfiguration dispenser;

    private final QueueConfiguration queue;

    private final ThemeConfiguration theme;

    private final TokenConfiguration token;

    private final SmsConfiguration sms;

    private final SurveyConfiguration survey;

    private final MeetingConfiguration meeting;

    private final Home home;

//    private final AppointmentConfiguration appointment;

    private final KpiConfiguration email;

//    private final BreakConfiguration breakConfiguration;

    private ZonedDateTime time; // = LocalDateTime.now();

    private final List<String> activeProfiles = new ArrayList<>(0);

    private final License license;
    public String getMode(){
        if (activeProfiles.contains("enterprise"))
            return "enterprise";
        else
            return "essential";
    }

    public ConfigurationResource(AudioConfiguration audio, DispenserConfiguration dispenser, QueueConfiguration queue, ThemeConfiguration theme, TokenConfiguration token, SmsConfiguration smsConfiguration, SurveyConfiguration survey, MeetingConfiguration meetingConfiguration, KpiConfiguration email,Home home,String[] activeProfiles, License license) {
        this.audio = audio;
        this.dispenser = dispenser;
        this.queue = queue;
        this.theme = theme;
        this.token = token;
        this.sms = smsConfiguration;
        this.home = home;
        this.survey = survey;
//        this.appointment = appointment;
        this.meeting = meetingConfiguration;
        this.email = email;
        this.activeProfiles.addAll(Arrays.asList(activeProfiles));
        log.info(this.home + "");

        this.time = ZonedDateTime.now();

        if (this.home != null ) {
            if (this.home.getTimeZoneId() != null) {
                this.time = ZonedDateTime.now(home.getTimeZoneId());
            }
        }
//        this.breakConfiguration = breakConfiguration;
        this.license = license;

        log.info(home.getTimeZoneId() + "");
        //log.info(time.atZone(home.getTimeZoneId()) + "india time");
        log.info("new date: {}", ZonedDateTime.of(LocalDateTime.now(), home.getTimeZoneId()).withZoneSameInstant(ZoneId.of("Asia/Kolkata")));
        log.info("new date: {}", ZonedDateTime.of(LocalDateTime.now(), ZoneId.of( "Asia/Dubai" )).withZoneSameInstant(ZoneId.of("Asia/Kolkata")));
        LocalDateTime ldt = LocalDateTime.now(); //Local date time
        ZoneId zoneId = ZoneId.of( "Asia/Kolkata" );  //Zone information
        ZonedDateTime zdtAtAsia = ldt.atZone( zoneId );	//Local time in Asia timezone
        ZonedDateTime zdtAtET = zdtAtAsia
                .withZoneSameInstant( ZoneId.of( "America/New_York" ) ); //Sama time in ET timezone
        log.info(ldt + "");
        log.info(zdtAtAsia + "");
        log.info(zdtAtET + "");

        log.info(ZonedDateTime.now(ZoneId.of("Europe/Paris")) + "");
        log.info(ZonedDateTime.now(home.getTimeZoneId()) + "");
    }
}
