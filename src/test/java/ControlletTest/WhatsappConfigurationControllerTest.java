package ControlletTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.queberry.que.config.Whatsapp.WhatsappConfiguration;
import io.queberry.que.config.Whatsapp.WhatsappConfigurationController;
import io.queberry.que.config.Whatsapp.WhatsappConfigurationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WhatsappConfigurationController.class)
class WhatsappConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WhatsappConfigurationRepository whatsappConfigurationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetWhatsAppConfig_returnsConfig() throws Exception {
        WhatsappConfiguration config = new WhatsappConfiguration();
        Mockito.when(whatsappConfigurationRepository.findAll())
                .thenReturn(Collections.singletonList(config));

        mockMvc.perform(get("/api/config/whatsapp"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void testEditSalesforceConfig_createsOrUpdates() throws Exception {
        WhatsappConfiguration existingConfig = new WhatsappConfiguration();
        WhatsappConfigurationController.Resource resource = new WhatsappConfigurationController.Resource(true, "token123", "1234567890", "businessId", "appId", "phoneId");

        Mockito.when(whatsappConfigurationRepository.findAll())
                .thenReturn(Collections.singletonList(existingConfig));
        Mockito.when(whatsappConfigurationRepository.save(Mockito.any(WhatsappConfiguration.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/api/config/whatsapp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.token").value("token123"));
    }

    @Test
    void testGetWhatsAppTenantConfig_found() throws Exception {
        WhatsappConfiguration config = new WhatsappConfiguration();
        Mockito.when(whatsappConfigurationRepository.findByMobile("1234567890"))
                .thenReturn(Optional.of(config));

        mockMvc.perform(get("/api/whatsapp/1234567890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void testGetWhatsAppTenantConfig_notFound() throws Exception {
        Mockito.when(whatsappConfigurationRepository.findByMobile("0000000000"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/whatsapp/0000000000"))
                .andExpect(status().isPreconditionFailed())
                .andExpect(content().string("Whatsapp not configured for any tenant with given mobile number"));
    }
}
