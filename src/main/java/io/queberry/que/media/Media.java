package io.queberry.que.media;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.queberry.que.entity.AggregateRoot;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.MediaType;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Media extends AggregateRoot<Media> {

    private String name;

    private String fileId;

    @Enumerated(EnumType.STRING)
    private MediaType type;

    @JsonCreator
    public Media(String name, String fileId, MediaType type) {
        this.name = name;
        this.fileId = fileId;
        this.type = type;
    }
    @Getter
    public enum MediaType{
        IMAGE,
        VIDEO,
        YOUTUBE_LINK;
    }
}
