package io.queberry.que.Controller;

import io.queberry.que.Entity.CloudloomController;
import io.queberry.que.Services.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@CloudloomController
@RequiredArgsConstructor
public class ConfigurationController {

    private final ConfigurationService configurationService;

//    @GetMapping("/config")
//    public ResponseEntity config(){
//        return ResponseEntity.ok(configurationService.getGlobalConfiguration());
//    }
//
//    @GetMapping("/main/config")
//    public ResponseEntity branchConfig(){
//        return ResponseEntity.ok(configurationService.getMainConfiguration());
//    }
}
