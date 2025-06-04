package ControlletTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.queberry.que.config.Meeting.MeetingConfiguration;
import io.queberry.que.config.Meeting.MeetingConfigurationController;
import io.queberry.que.config.Meeting.MeetingConfigurationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MeetingConfigurationController.class)
public class MeetingConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MeetingConfigurationRepository meetingConfigurationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAudioConfig_ReturnsConfig_WhenExists() throws Exception {
        MeetingConfiguration config = new MeetingConfiguration("SURV123");
        Mockito.when(meetingConfigurationRepository.findAll())
                .thenReturn(Collections.singletonList(config));

        mockMvc.perform(get("/api/config/meeting"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.surveyId").value("SURV123"));
    }

    @Test
    void testGetAudioConfig_ReturnsNull_WhenNoneExists() throws Exception {
        Mockito.when(meetingConfigurationRepository.findAll())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/config/meeting"))
                .andExpect(status().isOk())
                .andExpect(content().string("")); // No content (null returned)
    }

    @Test
    void testEditAudioConfig_UpdatesSurveyId() throws Exception {
        MeetingConfiguration existing = new MeetingConfiguration("OLD_ID");
        MeetingConfiguration updated = new MeetingConfiguration("NEW_ID");

        MeetingConfigurationController.MeetingConfigurationResource resource =
                new MeetingConfigurationController.MeetingConfigurationResource("NEW_ID");

        Mockito.when(meetingConfigurationRepository.findAll())
                .thenReturn(Collections.singletonList(existing));

        Mockito.when(meetingConfigurationRepository.save(Mockito.any()))
                .thenReturn(updated);

        mockMvc.perform(put("/api/config/meeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.surveyId").value("NEW_ID"));
    }
}
