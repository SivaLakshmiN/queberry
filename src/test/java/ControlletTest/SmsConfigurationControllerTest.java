package ControlletTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.queberry.que.config.Sms.SmsConfiguration;
import io.queberry.que.config.Sms.SmsConfigurationController;
import io.queberry.que.config.Sms.SmsConfigurationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SmsConfigurationController.class)
class SmsConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SmsConfigurationRepository smsConfigurationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetSmsConfig_whenExists() throws Exception {
        SmsConfiguration config = new SmsConfiguration();
        config.setEnabled(true);
        config.setUrl("https://sms.example.com");
        config.setUsername("user");
        config.setPassword("pass");
        config.setProvider(SmsConfiguration.Provider.ETISALAT);

        Mockito.when(smsConfigurationRepository.findAll())
                .thenReturn(java.util.List.of(config));

        mockMvc.perform(get("/api/config/sms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.url").value("https://sms.example.com"))
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.provider").value("ETISALAT"));
    }

    @Test
    void testGetSmsConfig_whenEmpty_returnsNull() throws Exception {
        Mockito.when(smsConfigurationRepository.findAll())
                .thenReturn(java.util.List.of());

        mockMvc.perform(get("/api/config/sms"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));  // Returns null -> empty response
    }

    @Test
    void testEditSmsConfig_updatesExisting() throws Exception {
        SmsConfiguration existing = new SmsConfiguration();
        existing.setEnabled(false);
        existing.setUrl("http://old-url.com");
        existing.setUsername("oldUser");
        existing.setPassword("oldPass");
        existing.setProvider(SmsConfiguration.Provider.DP_REST);

        SmsConfiguration updateResource = new SmsConfiguration();
        updateResource.setEnabled(true);
        updateResource.setUrl("https://new-url.com");
        updateResource.setUsername("newUser");
        updateResource.setPassword("newPass");
        updateResource.setProvider(SmsConfiguration.Provider.ETISALAT);

        Mockito.when(smsConfigurationRepository.findAll())
                .thenReturn(java.util.List.of(existing));
        Mockito.when(smsConfigurationRepository.save(Mockito.any(SmsConfiguration.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/api/config/sms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateResource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.url").value("https://new-url.com"))
                .andExpect(jsonPath("$.username").value("newUser"))
                .andExpect(jsonPath("$.provider").value("ETISALAT"));
    }

    @Test
    void testGetSmsProviders() throws Exception {
        mockMvc.perform(get("/api/config/sms/providers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[?(@ == 'AJMAN_BANK_LOCAL')]").exists())
                .andExpect(jsonPath("$[?(@ == 'ETISALAT')]").exists())
                .andExpect(jsonPath("$[?(@ == 'RTA')]").exists());
    }
}
