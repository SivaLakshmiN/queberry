package io.queberry.que.Entity;
import io.queberry.que.Controller.MeetingConfigurationController;
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
public class MeetingConfiguration extends AggregateRoot<MeetingConfiguration> {

    private String surveyId;

    public MeetingConfiguration change(MeetingConfigurationController.MeetingConfigurationResource resource){
        this.surveyId = resource.getSurveyId();
        return this;
    }
}
