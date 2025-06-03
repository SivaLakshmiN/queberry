package io.queberry.que.config.Sms;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SmsConfigurationController {

    private final SmsConfigurationRepository smsConfigurationRepository;

    public SmsConfigurationController(SmsConfigurationRepository smsConfigurationRepository) {
        this.smsConfigurationRepository = smsConfigurationRepository;
    }

    @GetMapping("/config/sms")
    public ResponseEntity getAudioConfig(){
        return ResponseEntity.ok(smsConfigurationRepository.findAll().stream().findFirst().orElse(null));
    }

    @PutMapping("/config/sms")
    public ResponseEntity editAudioCnnfig(@RequestBody SmsConfiguration resource){
        SmsConfiguration smsConfiguration = smsConfigurationRepository.findAll().stream().findFirst().orElse(null);
        smsConfiguration.change(resource);
        smsConfiguration = smsConfigurationRepository.save(smsConfiguration);
        return ResponseEntity.ok(smsConfiguration);
    }

    @GetMapping("/config/sms/providers")
    public ResponseEntity getSmsProvider(){
        return ResponseEntity.ok(SmsConfiguration.Provider.values());
    }

}
