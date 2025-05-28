package io.queberry.que.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.queberry.que.Controller.AudioConfigurationController;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor(force = true)
public class AudioConfiguration extends AggregateRoot<AudioConfiguration> {

    @Column(columnDefinition = "bit default 1")
    private boolean bell;

    @Column(columnDefinition = "bit default 1")
    private boolean announcement;
    private Type announcementType;

    @Column(unique = true)
    private String branchKey;

    public AudioConfiguration change(AudioConfigurationController.AudioConfigurationResource resource){
        this.bell = resource.isBell();
        this.announcement = resource.isAnnouncement();
        return andEvent(ConfigurationEvent.of(this));
    }

    public AudioConfiguration(boolean bell, boolean announcement, String branchKey, Type announcementType) {
        this.bell = bell;
        this.announcement = announcement;
        this.branchKey = branchKey;
        this.announcementType = announcementType;
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

    public enum Type{
        SINGLE_TOKEN,
        BOTH_ENGLISH,
        BOTH_ARABIC,
        BOTH_TOKEN
    }
}
