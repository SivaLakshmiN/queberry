package ControlletTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.queberry.que.config.Audio.AudioConfiguration;
import io.queberry.que.config.Audio.AudioConfigurationController;
import io.queberry.que.config.Audio.AudioConfigurationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AudioConfigurationController.class)
public class AudioConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AudioConfigurationRepository audioConfigurationRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetAudioConfig() throws Exception {
        AudioConfiguration config = new AudioConfiguration(true, true, "branch1", AudioConfiguration.Type.BOTH_ENGLISH);
        Mockito.when(audioConfigurationRepository.findByBranchKey("branch1")).thenReturn(Optional.of(config));

        mockMvc.perform(get("/api/config/audio").header("X-TenantID", "branch1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchKey").value("branch1"))
                .andExpect(jsonPath("$.bell").value(true));
    }

    @Test
    void testGetBranchAudioConfig() throws Exception {
        AudioConfigurationController.AudioConfigurationResource resource =
                new AudioConfigurationController.AudioConfigurationResource(true, true, "branch1", AudioConfiguration.Type.BOTH_ENGLISH);

        AudioConfiguration config = new AudioConfiguration(true, true, "branch1", AudioConfiguration.Type.BOTH_ENGLISH);
        Mockito.when(audioConfigurationRepository.findByBranchKey("branch1")).thenReturn(Optional.of(config));

        mockMvc.perform(put("/api/config/branchAudio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchKey").value("branch1"))
                .andExpect(jsonPath("$.announcement").value(true));
    }

    @Test
    void testAssignAudioConfig_New() throws Exception {
        AudioConfigurationController.AudioConfigurationResource resource =
                new AudioConfigurationController.AudioConfigurationResource(true, false, "branch2", AudioConfiguration.Type.SINGLE_TOKEN);

        AudioConfiguration savedConfig = new AudioConfiguration(true, false, "branch2", AudioConfiguration.Type.SINGLE_TOKEN);

        Mockito.when(audioConfigurationRepository.findByBranchKey("branch2")).thenReturn(Optional.empty());
        Mockito.when(audioConfigurationRepository.save(any(AudioConfiguration.class))).thenReturn(savedConfig);

        mockMvc.perform(put("/api/config/audio/assign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchKey").value("branch2"))
                .andExpect(jsonPath("$.announcementType").value("SINGLE_TOKEN"));
    }

    @Test
    void testGetAllAudioSettings() throws Exception {
        AudioConfiguration config1 = new AudioConfiguration(true, true, "branch1", AudioConfiguration.Type.BOTH_TOKEN);
        AudioConfiguration config2 = new AudioConfiguration(false, true, "branch2", AudioConfiguration.Type.BOTH_ARABIC);

        Mockito.when(audioConfigurationRepository.findAll()).thenReturn(List.of(config1, config2));

        mockMvc.perform(get("/api/config/audio/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
