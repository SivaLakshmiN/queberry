package io.queberry.que.Controller;
import io.queberry.que.Entity.CloudloomController;
import io.queberry.que.Entity.MeetingConfiguration;
import io.queberry.que.Repository.MeetingConfigurationRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CloudloomController
@RequiredArgsConstructor
public class MeetingConfigurationController {

    private final MeetingConfigurationRepository meetingConfigurationRepository;

    @GetMapping("/config/meeting")
    public ResponseEntity getAudioConfig(){
        return ResponseEntity.ok(meetingConfigurationRepository.findAll().stream().findFirst().orElse(null));
    }


    @PutMapping("/config/meeting")
    public ResponseEntity editAudioConfig(@RequestBody MeetingConfigurationResource resource){
        MeetingConfiguration meetingConfiguration = meetingConfigurationRepository.findAll().stream().findFirst().orElse(null);
        meetingConfiguration.change(resource);
        meetingConfiguration = meetingConfigurationRepository.save(meetingConfiguration);
        return ResponseEntity.ok(meetingConfiguration);
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MeetingConfigurationResource{
        private String surveyId;
    }
}
