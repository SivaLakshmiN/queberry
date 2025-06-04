package ControlletTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.queberry.que.config.Queue.QueueConfiguration;
import io.queberry.que.config.Queue.QueueConfigurationController;
import io.queberry.que.config.Queue.QueueConfigurationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QueueConfigurationController.class)
class QueueConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QueueConfigurationRepository queueConfigurationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllStrategies() throws Exception {
        mockMvc.perform(get("/api/config/queue/strategies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value("FIFO"));
    }

    @Test
    void testGetQueueConfig_whenExists() throws Exception {
        QueueConfiguration config = new QueueConfiguration(
                QueueConfiguration.QueueStrategy.FIFO, 10, 20, QueueConfiguration.ServicePriority.USER, "BR001"
        );
        Mockito.when(queueConfigurationRepository.findByBranchKey("BR001"))
                .thenReturn(Optional.of(config));

        mockMvc.perform(get("/api/config/queue/BR001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchKey").value("BR001"))
                .andExpect(jsonPath("$.strategy").value("FIFO"));
    }

    @Test
    void testGetQueueConfig_whenNotExists() throws Exception {
        Mockito.when(queueConfigurationRepository.findByBranchKey("BR002"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/config/queue/BR002"))
                .andExpect(status().isPreconditionFailed());
    }

    @Test
    void testEditQueueConfig_whenExists() throws Exception {
        QueueConfiguration existing = new QueueConfiguration(
                QueueConfiguration.QueueStrategy.FIFO, 10, 20, QueueConfiguration.ServicePriority.USER, "BR001"
        );

        QueueConfiguration updated = new QueueConfiguration(
                QueueConfiguration.QueueStrategy.SERVICE_PRIORITY, 15, 30, QueueConfiguration.ServicePriority.COUNTER, "BR001"
        );

        Mockito.when(queueConfigurationRepository.findByBranchKey("BR001")).thenReturn(Optional.of(existing));
        Mockito.when(queueConfigurationRepository.save(Mockito.any())).thenReturn(updated);

        mockMvc.perform(put("/api/config/queue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.strategy").value("SERVICE_PRIORITY"))
                .andExpect(jsonPath("$.slaWait").value(15));
    }

    @Test
    void testEditQueueConfig_whenNotExists_createsNew() throws Exception {
        QueueConfiguration newConfig = new QueueConfiguration(
                QueueConfiguration.QueueStrategy.FIFO, 10, 20, QueueConfiguration.ServicePriority.BOTH, "NEW01"
        );

        Mockito.when(queueConfigurationRepository.findByBranchKey("NEW01")).thenReturn(Optional.empty());
        Mockito.when(queueConfigurationRepository.save(Mockito.any())).thenReturn(newConfig);

        mockMvc.perform(put("/api/config/queue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newConfig)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchKey").value("NEW01"));
    }

    @Test
    void testSaveQueueConfig() throws Exception {
        QueueConfiguration config = new QueueConfiguration(
                QueueConfiguration.QueueStrategy.FIFO, 5, 15, QueueConfiguration.ServicePriority.USER, "SAVE01"
        );

        Mockito.when(queueConfigurationRepository.save(Mockito.any())).thenReturn(config);

        mockMvc.perform(post("/api/config/queue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(config)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchKey").value("SAVE01"));
    }
}
