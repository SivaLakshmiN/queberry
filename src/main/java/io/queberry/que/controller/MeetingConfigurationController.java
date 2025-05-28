package io.queberry.que.controller;

import io.queberry.que.config.MeetingConfiguration;
import io.queberry.que.repository.MeetingConfigurationRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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
