package io.queberry.que.Controller;

import io.queberry.que.Entity.AudioConfiguration;
import io.queberry.que.Entity.CloudloomController;
import io.queberry.que.Repository.AudioConfigurationRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Objects;


@Slf4j
@CloudloomController
@RequiredArgsConstructor
public class AudioConfigurationController {

    private final AudioConfigurationRepository audioConfigurationRepository;

    @GetMapping("/config/audio")
    public ResponseEntity<?> getAudioConfig(HttpServletRequest request){
        log.info("request header in transferToUser {}",request.getHeader("X-TenantID"));
        return ResponseEntity.ok(Objects.requireNonNull(audioConfigurationRepository.findByBranchKey(request.getHeader("X-TenantID"))));
    }

    @PutMapping("/config/audio")
    public ResponseEntity editAudioConfig(@RequestBody AudioConfigurationResource resource){
        AudioConfiguration audioConfiguration = audioConfigurationRepository.findAll().stream().findFirst().orElse(null);
        audioConfiguration.change(resource);
        audioConfiguration = audioConfigurationRepository.save(audioConfiguration);
        return ResponseEntity.ok(audioConfiguration);
    }

    @PutMapping("/config/branchAudio")
    public ResponseEntity<?> getBranchAudioConfig(@RequestBody AudioConfigurationResource resource){
        return ResponseEntity.ok(Objects.requireNonNull(audioConfigurationRepository.findByBranchKey(resource.getBranchKey())));
    }

    @PutMapping("/config/audio/assign")
    public ResponseEntity<?> assignAudioConfig(@RequestBody AudioConfigurationResource resource){
        AudioConfiguration audioConfiguration = audioConfigurationRepository.findByBranchKey(resource.getBranchKey()).orElse(null);
        if(audioConfiguration ==  null){
            audioConfiguration = new AudioConfiguration(resource.bell, resource.announcement, resource.getBranchKey(), resource.getAnnouncementType());
        }else {
            audioConfiguration.setBell(resource.bell);
            audioConfiguration.setAnnouncement(resource.announcement);
            audioConfiguration.setAnnouncementType(resource.announcementType);
        }
        audioConfiguration = audioConfigurationRepository.save(audioConfiguration);
        return ResponseEntity.ok(audioConfiguration);
    }

//    @PostMapping("/config/audio")
//    public ResponseEntity createConfig(@RequestBody AudioConfigurationResource resource){
//        AudioConfiguration audioConfiguration = new AudioConfiguration(resource.isBell(), resource.isAnnouncement());
//        audioConfiguration = audioConfigurationRepository.save(audioConfiguration);
//        return ResponseEntity.ok(audioConfiguration);
//    }

    @GetMapping("/config/audio/all")
    public ResponseEntity<?> getAllAudioSettings(){
       List<AudioConfiguration> data = audioConfigurationRepository.findAll();
       return ResponseEntity.ok(data);
    }



    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AudioConfigurationResource{
        private boolean bell;
        private boolean announcement;
        private String branchKey;
        private AudioConfiguration.Type announcementType;
    }

}
