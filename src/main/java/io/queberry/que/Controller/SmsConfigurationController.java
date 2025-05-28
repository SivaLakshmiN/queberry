package io.queberry.que.Controller;

import io.queberry.que.Entity.CloudloomController;
import io.queberry.que.Entity.SmsConfiguration;
import io.queberry.que.Repository.SmsConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CloudloomController
@RequiredArgsConstructor
public class SmsConfigurationController {

    private final SmsConfigurationRepository smsConfigurationRepository;

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
