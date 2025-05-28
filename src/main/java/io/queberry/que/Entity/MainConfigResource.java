package io.queberry.que.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Getter
@Setter
@ToString(callSuper = true)
@Slf4j
public class MainConfigResource {

    private final QueueConfiguration queue;

    private final Home home;

    private ZonedDateTime time;

    private final List<String> activeProfiles = new ArrayList<>(0);

    private final License license;
    public String getMode(){
        if (activeProfiles.contains("enterprise"))
            return "enterprise";
        else
            return "essential";
    }

    public MainConfigResource(QueueConfiguration queue, Home home, String[] activeProfiles, QueueConfiguration queue1, License license) {
        this.home = home;
        this.queue = queue;
        this.activeProfiles.addAll(Arrays.asList(activeProfiles));
        this.time = ZonedDateTime.now();

        if (this.home != null ) {
            if (this.home.getTimeZoneId() != null) {
                this.time = ZonedDateTime.now(home.getTimeZoneId());
            }
        }
        this.license = license;
    }
}
