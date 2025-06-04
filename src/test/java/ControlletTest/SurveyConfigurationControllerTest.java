package ControlletTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.queberry.que.config.Survey.SurveyConfiguration;
import io.queberry.que.config.Survey.SurveyConfigurationController;
import io.queberry.que.config.Survey.SurveyConfigurationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SurveyConfigurationController.class)
class SurveyConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SurveyConfigurationRepository surveyConfigurationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetSurveyConfig_whenExists() throws Exception {
        SurveyConfiguration config = new SurveyConfiguration();
        config.setEnabled(true);
        config.setSms(true);
        config.setEmail(false);
        config.setDevice(true);
        config.setTimeout(30);
        config.setMessage("Test message");
        config.setTrigger(SurveyConfiguration.Trigger.AFTER);
        config.setSmsTextEnglish("English SMS");
        config.setSmsTextSecondary("Secondary SMS");
        config.setEmailSubject("Email Subject");
        config.setEmailContent("Email Content");
        config.setRetriggerDays(5);
        config.setExpiryDays(7);
        config.setSurveyUrl("http://survey.url");

        Mockito.when(surveyConfigurationRepository.findAll())
                .thenReturn(java.util.List.of(config));

        mockMvc.perform(get("/api/config/survey"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.sms").value(true))
                .andExpect(jsonPath("$.email").value(false))
                .andExpect(jsonPath("$.device").value(true))
                .andExpect(jsonPath("$.timeout").value(30))
                .andExpect(jsonPath("$.message").value("Test message"))
                .andExpect(jsonPath("$.trigger").value("AFTER"))
                .andExpect(jsonPath("$.smsTextEnglish").value("English SMS"))
                .andExpect(jsonPath("$.smsTextSecondary").value("Secondary SMS"))
                .andExpect(jsonPath("$.emailSubject").value("Email Subject"))
                .andExpect(jsonPath("$.emailContent").value("Email Content"))
                .andExpect(jsonPath("$.retriggerDays").value(5))
                .andExpect(jsonPath("$.expiryDays").value(7))
                .andExpect(jsonPath("$.surveyUrl").value("http://survey.url"));
    }

    @Test
    void testGetSurveyConfig_whenEmpty_returnsDefault() throws Exception {
        Mockito.when(surveyConfigurationRepository.findAll())
                .thenReturn(java.util.List.of());

        mockMvc.perform(get("/api/config/survey"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(false))  // default new SurveyConfiguration has enabled = false
                .andExpect(jsonPath("$.sms").value(false));
    }

    @Test
    void testEditSurveyConfig_updatesExisting() throws Exception {
        SurveyConfiguration existing = new SurveyConfiguration();
        existing.setEnabled(false);
        existing.setMessage("Old message");

        SurveyConfiguration update = new SurveyConfiguration();
        update.setEnabled(true);
        update.setMessage("Updated message");
        update.setSms(true);

        Mockito.when(surveyConfigurationRepository.findAll())
                .thenReturn(java.util.List.of(existing));
        Mockito.when(surveyConfigurationRepository.save(Mockito.any(SurveyConfiguration.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/api/config/survey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.message").value("Updated message"))
                .andExpect(jsonPath("$.sms").value(true));
    }
}
