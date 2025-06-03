package io.queberry.que.config;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ConfigurationController {


    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

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
