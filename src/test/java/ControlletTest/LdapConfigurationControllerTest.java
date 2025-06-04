package ControlletTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.queberry.que.config.Ldap.LdapConfiguration;
import io.queberry.que.config.Ldap.LdapConfigurationController;
import io.queberry.que.config.Ldap.LdapConfigurationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LdapConfigurationController.class)
public class LdapConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LdapConfigurationRepository ldapConfigurationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetLdapConfig_returnsDefaultIfNonePresent() throws Exception {
        Mockito.when(ldapConfigurationRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/config/ldap"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(false));
    }

    @Test
    public void testGetLdapConfig_returnsExistingConfig() throws Exception {
        LdapConfiguration config = new LdapConfiguration(true, "ldap.example.com", "dc=example,dc=com",
                "uid", "value", "admin", "pass", "cn", "uid");

        Mockito.when(ldapConfigurationRepository.findAll()).thenReturn(Collections.singletonList(config));

        mockMvc.perform(get("/api/config/ldap"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.host").value("ldap.example.com"));
    }

    @Test
    public void testPutLdapConfig_updatesAndReturnsConfig() throws Exception {
        LdapConfiguration existing = new LdapConfiguration(false, "", "", "", "", "", "", "", "");
        LdapConfiguration updated = new LdapConfiguration(true, "ldap.new.com", "dc=test,dc=com", "uid", "val", "user", "pwd", "cn", "login");

        Mockito.when(ldapConfigurationRepository.findAll()).thenReturn(Collections.singletonList(existing));
        Mockito.when(ldapConfigurationRepository.save(Mockito.any(LdapConfiguration.class))).thenReturn(updated);

        mockMvc.perform(put("/api/config/ldap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.host").value("ldap.new.com"))
                .andExpect(jsonPath("$.enabled").value(true));
    }
}
