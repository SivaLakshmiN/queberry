package io.queberry.que.Controller;

import io.queberry.que.Entity.CloudloomController;
import io.queberry.que.Entity.TenantContext;
import io.queberry.que.Entity.WhatsappConfiguration;
import io.queberry.que.Repository.WhatsappConfigurationRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@CloudloomController
@RequiredArgsConstructor
public class WhatsappConfigurationController {

    private final WhatsappConfigurationRepository whatsappConfigurationRepository;

    @GetMapping("/config/whatsapp")
    public ResponseEntity getWhatsAppConfig(){
        log.info("in get whatsapp");
            return ResponseEntity.ok(whatsappConfigurationRepository.findAll().stream().findFirst().orElse(new WhatsappConfiguration()));
    }

    @PutMapping("/config/whatsapp")
    public ResponseEntity editSalesforceConfig(@RequestBody Resource resource){
        WhatsappConfiguration whatsappConfiguration = whatsappConfigurationRepository.findAll().stream().findFirst().orElse(new WhatsappConfiguration());
        whatsappConfiguration.change(resource);
        whatsappConfiguration = whatsappConfigurationRepository.save(whatsappConfiguration);
        if(!TenantContext.getCurrentTenant().equals("queberry")) {
            qbwhatsapp(resource,TenantContext.getCurrentTenant());
        }
        return ResponseEntity.ok(whatsappConfiguration);
    }

    public void qbwhatsapp(Resource resource, String tenant){

        TenantContext.setCurrentTenant("queberry");
        Optional<WhatsappConfiguration> whatsappConfiguration = whatsappConfigurationRepository.findByTenant(tenant);
        if(whatsappConfiguration.isPresent()){
            whatsappConfiguration.get().change(resource);
            whatsappConfigurationRepository.save(whatsappConfiguration.get());
        } else {
            WhatsappConfiguration wc = new WhatsappConfiguration();
            wc.change(resource);
            whatsappConfigurationRepository.save(wc);
        }
    }

    @GetMapping("/whatsapp/{mobile}")
    public ResponseEntity getWhatsAppTenantConfig(@PathVariable("mobile") String mobile){
        log.info("in get whatsapp:{}", mobile);
        Optional<WhatsappConfiguration> whatsappConfiguration = whatsappConfigurationRepository.findByMobile(mobile);
        if(whatsappConfiguration.isPresent())
            return ResponseEntity.ok(whatsappConfiguration.get());
        else
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Whatsapp not configured for any tenant with given mobile number");
    }

    @GetMapping("/dispenser/whatsppsend")
    public void sendmsg() {
        WhatsappConfiguration config = whatsappConfigurationRepository.findAll().get(0);
        log.info("in send:{}", config.getToken());

        String API_URL_SEND = "https://graph.facebook.com/v20.0/557226097473770/messages";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(config.getToken());  // Cleaner token handling

// Correct JSON Payload
        JSONObject payload = new JSONObject();
        payload.put("messaging_product", "whatsapp");  // Lowercase, and ensure it's a string
        payload.put("to", "971506317418");
        payload.put("type", "text");

// Text message object
        JSONObject textObj = new JSONObject();
        textObj.put("body", "hi");
        payload.put("text", textObj);

        HttpEntity<String> requestEntity = new HttpEntity<>(payload.toString(), httpHeaders);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    API_URL_SEND, HttpMethod.POST, requestEntity, String.class
            );
            log.info("Response: {}", responseEntity.getBody());
        } catch (HttpClientErrorException e) {
            log.error("HTTP Status Code: {}", e.getStatusCode());
            log.error("Response Body: {}", e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Unexpected Error: ", e);
        }

    }

        @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Resource{
        private boolean enabled;
        private String token;
        private String businessId;
        private String mobile;
        private String appId;
        private String phoneNumberId;
    }
}
