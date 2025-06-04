package ControlletTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.queberry.que.config.Kpi.KpiConfiguration;
import io.queberry.que.config.Kpi.KpiConfigurationController;
import io.queberry.que.config.Kpi.KpiConfigurationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KpiConfigurationController.class)
public class KpiConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private KpiConfigurationRepository kpiConfigurationRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetKpiConfig() throws Exception {
        KpiConfiguration config = new KpiConfiguration(true, "EX", "tenant1", "client1", "secret", "sender",
                true, "smtp", "localhost", 587, "user", "pass", "from@mail.com",
                true, true, true, "subject", "uploadId");

        Mockito.when(kpiConfigurationRepository.findAll()).thenReturn(Collections.singletonList(config));

        mockMvc.perform(get("/api/config/kpi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.exchangeType").value("EX"));
    }

    @Test
    void testEditKpiConfig() throws Exception {
        KpiConfiguration resource = new KpiConfiguration(true, "EX", "tenant1", "client1", "secret", "sender",
                true, "smtp", "localhost", 587, "user", "pass", "from@mail.com",
                true, true, true, "subject", "uploadId");

        Mockito.when(kpiConfigurationRepository.findAll()).thenReturn(Collections.singletonList(new KpiConfiguration()));
        Mockito.when(kpiConfigurationRepository.save(any())).thenReturn(resource);

        mockMvc.perform(put("/api/config/kpi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value("client1"))
                .andExpect(jsonPath("$.enabled").value(true));
    }
}
