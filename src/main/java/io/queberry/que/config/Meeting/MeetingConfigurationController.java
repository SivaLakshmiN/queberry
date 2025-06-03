package io.queberry.que.config.Meeting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MeetingConfigurationController {

    private final MeetingConfigurationRepository meetingConfigurationRepository;

    public MeetingConfigurationController(MeetingConfigurationRepository meetingConfigurationRepository) {
        this.meetingConfigurationRepository = meetingConfigurationRepository;
    }

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
