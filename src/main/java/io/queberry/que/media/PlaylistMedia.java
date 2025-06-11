package io.queberry.que.media;

import io.queberry.que.entity.AuditedEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Table(name = "playlistMedia")
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PlaylistMedia extends AuditedEntity {

    private Integer duration;

    @Column(name = "playlist_media_order")
    private Integer order;

    @JoinTable(name="playlist_media__media",
            joinColumns = @JoinColumn(name = "playlist_media__ID",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "media__ID",
                    referencedColumnName = "id"))
    @ManyToOne
    private Media media;

    public PlaylistMedia(Media media,Integer duration) {
        new PlaylistMedia(media,duration,null);
    }

    public PlaylistMedia( Media media, Integer duration, Integer order) {
        this.duration = duration;
        this.order = order;
        this.media = media;
    }

    public Media getMediaResource(){
        return media;
    }
}

