package io.queberry.que.Controller;
import io.queberry.que.Entity.CloudloomController;
import io.queberry.que.Entity.SalesforceConfiguration;
import io.queberry.que.Repository.SalesforceConfigurationRepository;
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
public class SalesforceConfigurationController {

    private final SalesforceConfigurationRepository salesforceConfigurationRepository;

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
