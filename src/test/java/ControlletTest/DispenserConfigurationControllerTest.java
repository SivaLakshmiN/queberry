package ControlletTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.queberry.que.config.Dispenser.DispenserConfiguration;
import io.queberry.que.config.Dispenser.DispenserConfigurationController;
import io.queberry.que.config.Dispenser.DispenserConfigurationRepository;
import io.queberry.que.config.Theme.ThemeConfiguration;
import io.queberry.que.config.Theme.ThemeConfigurationRepository;
import io.queberry.que.entity.Language;
import io.queberry.que.service.ServiceRepository;
import io.queberry.que.counter.CounterRepository;
import io.queberry.que.employee.EmployeeRepository;
import io.queberry.que.session.SessionRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DispenserConfigurationController.class)
class DispenserConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DispenserConfigurationRepository dispenserConfigurationRepository;

    @MockitoBean
    private ThemeConfigurationRepository themeConfigurationRepository;

    @MockitoBean
    private ServiceRepository serviceRepository;

    @MockitoBean
    private CounterRepository counterRepository;

    @MockitoBean
    private EmployeeRepository employeeRepository;

    @MockitoBean
    private SessionRepository sessionRepository;

    @MockitoBean
    private EntityManager entityManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetDispenserConfig() throws Exception {
        DispenserConfiguration config = new DispenserConfiguration("Welcome", 1, true, "Closed", false,
                true, false, "branch123", true, true, true, false, false);
        Mockito.when(dispenserConfigurationRepository.findByBranchKey("branch123"))
                .thenReturn(Optional.of(config));

        mockMvc.perform(get("/api/config/dispenser").header("X-TenantID", "branch123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.welcomeMessage").value("Welcome"));
    }

    @Test
    void testEditDispenserConfig_New() throws Exception {
        DispenserConfigurationController.DispenserConfigurationResource resource =
                new DispenserConfigurationController.DispenserConfigurationResource("Welcome", 1, true, "Closed", false,
                        true, false, "branch123", true, true, true, false, List.of(new Language("en", "English")));

        Mockito.when(dispenserConfigurationRepository.findByBranchKey("branch123"))
                .thenReturn(Optional.empty());

        Mockito.when(dispenserConfigurationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(put("/api/config/dispenser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchKey").value("branch123"));
    }

    @Test
    void testEditDispenserConfig_UpdateExisting() throws Exception {
        DispenserConfiguration existing = new DispenserConfiguration("Old", 1, false, "Closed", false,
                false, false, "branch123", false, false, false, false, false);

        ThemeConfiguration theme = new ThemeConfiguration();
        Mockito.when(dispenserConfigurationRepository.findByBranchKey("branch123")).thenReturn(Optional.of(existing));
        Mockito.when(themeConfigurationRepository.findByBranchKey("branch123")).thenReturn(Optional.of(theme));
        Mockito.when(dispenserConfigurationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        DispenserConfigurationController.DispenserConfigurationResource resource =
                new DispenserConfigurationController.DispenserConfigurationResource("New Welcome", 5, true, "Open", true,
                        true, true, "branch123", true, true, true, true, List.of());

        mockMvc.perform(put("/api/config/dispenser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.welcomeMessage").value("New Welcome"));
    }

    @Test
    void testGetBranchDispenserConfig() throws Exception {
        DispenserConfigurationController.DispenserConfigurationResource resource =
                new DispenserConfigurationController.DispenserConfigurationResource("Welcome", 1, true, "Closed", false,
                        true, false, "branch123", true, true, true, false, List.of());

        Mockito.when(dispenserConfigurationRepository.findByBranchKey("branch123"))
                .thenReturn(Optional.of(new DispenserConfiguration("branch123")));

        mockMvc.perform(put("/api/config/dispenserConfig")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk());
    }

    @Test
    void testMigrateEndpoint() throws Exception {
        Mockito.when(entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0")).thenReturn(queryMock());
        Mockito.when(entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1")).thenReturn(queryMock());

        mockMvc.perform(get("/api/config/dispenser/migrate"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    private jakarta.persistence.Query queryMock() {
        jakarta.persistence.Query mockQuery = Mockito.mock(jakarta.persistence.Query.class);
        Mockito.when(mockQuery.executeUpdate()).thenReturn(1);
        return mockQuery;
    }
}
