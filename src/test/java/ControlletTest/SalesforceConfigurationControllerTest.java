package ControlletTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.queberry.que.config.Salesforce.SalesforceConfiguration;
import io.queberry.que.config.Salesforce.SalesforceConfigurationController;
import io.queberry.que.config.Salesforce.SalesforceConfigurationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SalesforceConfigurationController.class)
class SalesforceConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SalesforceConfigurationRepository salesforceConfigurationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetSalesforceConfig_whenExists() throws Exception {
        SalesforceConfiguration config = new SalesforceConfiguration(true, "https://example.com", "clientId", "secret");

        Mockito.when(salesforceConfigurationRepository.findAll())
                .thenReturn(java.util.List.of(config));

        mockMvc.perform(get("/api/config/salesforce"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.url").value("https://example.com"))
                .andExpect(jsonPath("$.clientId").value("clientId"));
    }

    @Test
    void testGetSalesforceConfig_whenEmpty_returnsNewInstance() throws Exception {
        Mockito.when(salesforceConfigurationRepository.findAll())
                .thenReturn(java.util.List.of());

        mockMvc.perform(get("/api/config/salesforce"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(false))
                .andExpect(jsonPath("$.url").doesNotExist())
                .andExpect(jsonPath("$.clientId").doesNotExist());
    }

    @Test
    void testEditSalesforceConfig_updatesExisting() throws Exception {
        SalesforceConfiguration existing = new SalesforceConfiguration(false, "http://old-url.com", "oldId", "oldSecret");
        SalesforceConfigurationController.SalesforceConfigurationResource updateResource =
                new SalesforceConfigurationController.SalesforceConfigurationResource(true, "https://new-url.com", "newId", "newSecret");

        Mockito.when(salesforceConfigurationRepository.findAll())
                .thenReturn(java.util.List.of(existing));
        Mockito.when(salesforceConfigurationRepository.save(Mockito.any(SalesforceConfiguration.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/api/config/salesforce")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateResource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.url").value("https://new-url.com"))
                .andExpect(jsonPath("$.clientId").value("newId"))
                .andExpect(jsonPath("$.clientSecret").value("newSecret"));
    }

    @Test
    void testEditSalesforceConfig_createsNewIfNoneExists() throws Exception {
        SalesforceConfigurationController.SalesforceConfigurationResource updateResource =
                new SalesforceConfigurationController.SalesforceConfigurationResource(true, "https://new-url.com", "newId", "newSecret");

        Mockito.when(salesforceConfigurationRepository.findAll())
                .thenReturn(java.util.List.of());
        Mockito.when(salesforceConfigurationRepository.save(Mockito.any(SalesforceConfiguration.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/api/config/salesforce")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateResource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.url").value("https://new-url.com"))
                .andExpect(jsonPath("$.clientId").value("newId"))
                .andExpect(jsonPath("$.clientSecret").value("newSecret"));
    }
}
