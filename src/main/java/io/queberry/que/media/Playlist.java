package io.queberry.que.media;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.queberry.que.entity.AggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Playlist extends AggregateRoot<Playlist> {

    private String name;

    @OrderBy("playlist_media_order ASC")
    @JoinTable(name="playlist__playlist_media",
            joinColumns = @JoinColumn(name = "playlist__ID",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_media__ID",
                    referencedColumnName = "id"))
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<PlaylistMedia> media = new ArrayList<>(0);

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Integer getDuration(){
        return this.media.stream().filter(med -> med.getDuration() != null).map(PlaylistMedia::getDuration).reduce(Integer::sum).orElse(0);
    }

    public Playlist media(Set<PlaylistMedia> playlistMedia){
        if (playlistMedia != null && playlistMedia.size() > 0)
            this.media.addAll(playlistMedia);
        return this;
    }

    @JsonCreator
    public Playlist(String name, Set<PlaylistMedia> media) {
        this.name = name;

        if(media != null && media.size() >0) {
            this.media.addAll(media);
        }

    }
}
