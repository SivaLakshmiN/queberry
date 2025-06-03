package io.queberry.que.config.Salesforce;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SalesforceConfigurationController {

    private final SalesforceConfigurationRepository salesforceConfigurationRepository;

    public SalesforceConfigurationController(SalesforceConfigurationRepository salesforceConfigurationRepository) {
        this.salesforceConfigurationRepository = salesforceConfigurationRepository;
    }

    @GetMapping("/config/salesforce")
    public ResponseEntity getSalesforceConfig(){
        return ResponseEntity.ok(salesforceConfigurationRepository.findAll().stream().findFirst().orElse(new SalesforceConfiguration()));
    }
    @PutMapping("/config/salesforce")
    public ResponseEntity editSalesforceConfig(@RequestBody SalesforceConfigurationResource resource){
        SalesforceConfiguration salesforceConfiguration = salesforceConfigurationRepository.findAll().stream().findFirst().orElse(new SalesforceConfiguration());
        salesforceConfiguration.change(resource);
        salesforceConfiguration = salesforceConfigurationRepository.save(salesforceConfiguration);
        return ResponseEntity.ok(salesforceConfiguration);
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SalesforceConfigurationResource{
        private boolean enabled;
        private String url;
        private String clientId;
        private String clientSecret;
    }

}
