package ControlletTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.queberry.que.config.Token.TokenConfiguration;
import io.queberry.que.config.Token.TokenConfigurationController;
import io.queberry.que.config.Token.TokenConfigurationRepository;
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

@WebMvcTest(TokenConfigurationController.class)
class TokenConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TokenConfigurationRepository tokenConfigurationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetTokenConfiguration_returnsFirstConfig() throws Exception {
        TokenConfiguration config = new TokenConfiguration(120);
        Mockito.when(tokenConfigurationRepository.findAll())
                .thenReturn(Collections.singletonList(config));

        mockMvc.perform(get("/api/config/token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenValidity").value(120));
    }

    @Test
    void testGetTokenConfiguration_returnsNullWhenEmpty() throws Exception {
        Mockito.when(tokenConfigurationRepository.findAll())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/config/token"))
                .andExpect(status().isOk())
                .andExpect(content().string("")); // returns null -> empty body
    }

    @Test
    void testEditTokenConfiguration_updatesExisting() throws Exception {
        TokenConfiguration existingConfig = new TokenConfiguration(100);
        TokenConfigurationController.TokenConfigurationResource resource = new TokenConfigurationController.TokenConfigurationResource(180);

        Mockito.when(tokenConfigurationRepository.findAll())
                .thenReturn(Collections.singletonList(existingConfig));

        Mockito.when(tokenConfigurationRepository.save(Mockito.any(TokenConfiguration.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/api/config/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenValidity").value(180));
    }

    @Test
    void testEditTokenConfiguration_nullExisting_throwsException() throws Exception {
        // If repository returns empty list, your current code will cause NullPointerException
        Mockito.when(tokenConfigurationRepository.findAll())
                .thenReturn(Collections.emptyList());

        TokenConfigurationController.TokenConfigurationResource resource = new TokenConfigurationController.TokenConfigurationResource(180);

        mockMvc.perform(put("/api/config/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().is5xxServerError());  // Because you do not handle null currently
    }
}
