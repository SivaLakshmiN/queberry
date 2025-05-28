package io.queberry.que.Entity;

import io.queberry.que.Repository.WhatsappConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class WhatsappEngine {

    private final WhatsappConfigurationRepository whatsappConfigurationRepository;

//    @Async
//    @TransactionalEventListener(fallbackExecution = true)
//    public void onAssistance(Assistance.AssistanceCalled assistanceCalled) {
//
//        try {
//            log.info("assistance called");
//            WhatsappConfiguration whatsappConfiguration = whatsappConfigurationRepository.findAll().get(0);
//            if (whatsappConfiguration.isEnabled()) {
//                if (assistanceCalled.getAssistance().getType().equals(Type.DISPENSER) && assistanceCalled.getAssistance().getMobile() != null && !assistanceCalled.getAssistance().getMobile().isEmpty() && assistanceCalled.getAssistance().getMedium().equals(Token.Medium.WHATSAPP) && !assistanceCalled.getAssistance().isHasAppointment()) {
//                    Assistance assistance = assistanceCalled.getAssistance();
//                    String counter = assistance.findLastSession().getCounter() != null ? assistance.findLastSession().getCounter().getCode() : "";
////                    log.info(home.getName() + "  " + assistance.getToken() + "   " + assistance.getService().getName() + "   ");
//                    String text = "Dear Valued Customer, your token " + assistance.getToken() + " has been called by Counter " + counter + ".";
//                    log.info("text:{}", text);
//                    send(whatsappConfiguration, assistance, text);
//                }
//            }
//        } catch (Exception e) {
//            log.error("Error while sending SMS token");
//            e.printStackTrace();
//        }
//    }

    //    @Async
//    @EventListener
//    public void onAssistance(Assistance.AssistanceTransferedToCounter assistanceTransferred) {
//
//        try {
//            log.info("transfer counter");
//            WhatsappConfiguration whatsappConfiguration = whatsappConfigurationRepository.findAll().get(0);
//            if (whatsappConfiguration.isEnabled()) {
//                if (assistanceTransferred.getAssistance().getMobile() != null && !assistanceTransferred.getAssistance().getMobile().isEmpty() && assistanceTransferred.getAssistance().getMedium().equals(Token.Medium.WHATSAPP)) {
//                    log.info("all cond satisfied");
//                    Assistance assistance = assistanceTransferred.getAssistance();
//                    String transferCounter = assistance.findLastSession().getTransferredToCounter().getCode();
//                    String text = "Dear Valued Customer, your token " + assistance.getToken() + " has been transferred to Counter " + transferCounter + ".";
//                    log.info("transferred sms text : " + text);
//                    send(whatsappConfiguration, assistance, text);
//                }
//            }
//        } catch (Exception e) {
//            log.error("Error while sending SMS token");
//            e.printStackTrace();
//        }
//    }

    public void send(WhatsappConfiguration config, Assistance a, String text) {
        log.info("in send:{}", config.getToken());

        String API_URL_SEND = "https://graph.facebook.com/v20.0/557226097473770/messages";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(config.getToken());  // Cleaner token handling

        JSONObject payload = new JSONObject();
        payload.put("messaging_product", "whatsapp");  // Lowercase, and ensure it's a string
        payload.put("to", a.getMobile());
        payload.put("type", "text");

// Text message object
        JSONObject textObj = new JSONObject();
        textObj.put("body", text);
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
//        String API_URL_SEND = "https://graph.facebook.com/v20.0/557226097473770/messages";
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("Authorization", "Bearer " + config.getToken());
//        httpHeaders.add("Content-Type", "application/json");
//
//        MultiValueMap<String, String> map_sms = new LinkedMultiValueMap<>();
//        map_sms.add("messaging_product", "whatsapp");
//        map_sms.add("to", a.getMobile());
//        map_sms.add("type", "text");
//        map_sms.add("body", text);
//
//        log.info("header, body set");
//        HttpEntity requestResourceHttpEntity = new HttpEntity<>(map_sms, httpHeaders);
////        HttpEntity<SMSSendRequest> requestResourceHttpEntity = new HttpEntity(smsRequest, httpHeaders);
//        RestTemplate restTemplate = new RestTemplate();
//        log.info("before apii call");
//        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(API_URL_SEND, HttpMethod.POST, requestResourceHttpEntity, JsonNode.class, new Object[0]);
//        log.info("response:{}", responseEntity.getStatusCode());
    }
}