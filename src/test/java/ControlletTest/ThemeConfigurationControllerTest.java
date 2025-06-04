package ControlletTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.queberry.que.config.Theme.ThemeConfiguration;
import io.queberry.que.config.Theme.ThemeConfigurationController;
import io.queberry.que.config.Theme.ThemeConfigurationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ThemeConfigurationController.class)
class ThemeConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ThemeConfigurationRepository themeConfigurationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetTheme_returnsThemeForBranchKey() throws Exception {
        String branchKey = "branch-123";

        ThemeConfiguration themeConfig = new ThemeConfiguration();
        themeConfig.setBranchKey(branchKey);
        themeConfig.setSignageLogo("logo1.png");
        themeConfig.setPrimaryColor("#FFFFFF");
        themeConfig.setShowTime(true);

        Mockito.when(themeConfigurationRepository.findByBranchKey(branchKey))
                .thenReturn(Optional.of(themeConfig));

        mockMvc.perform(get("/api/config/theme")
                        .header("X-TenantID", branchKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchKey").value(branchKey))
                .andExpect(jsonPath("$.signageLogo").value("logo1.png"))
                .andExpect(jsonPath("$.primaryColor").value("#FFFFFF"))
                .andExpect(jsonPath("$.showTime").value(true));
    }

    @Test
    void testGetTheme_returnsEmptyWhenNotFound() throws Exception {
        String branchKey = "branch-not-found";

        Mockito.when(themeConfigurationRepository.findByBranchKey(branchKey))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/config/theme")
                        .header("X-TenantID", branchKey))
                .andExpect(status().isOk())
                .andExpect(content().string("")); // Returns empty body if Optional.empty()
    }

    @Test
    void testGetThemeByBranch_returnsTheme() throws Exception {
        ThemeConfiguration themeConfig = new ThemeConfiguration();
        themeConfig.setBranchKey("branch-123");
        themeConfig.setSignageLogo("logo2.png");

        ThemeConfigurationController.Theme themeRequest = new ThemeConfigurationController.Theme();
        //themeRequest.setBranchKey("branch-123");

        Mockito.when(themeConfigurationRepository.findByBranchKey("branch-123"))
                .thenReturn(Optional.of(themeConfig));

        mockMvc.perform(put("/api/config/themeConfig")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(themeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchKey").value("branch-123"))
                .andExpect(jsonPath("$.signageLogo").value("logo2.png"));
    }

    @Test
    void testEditTheme_createsNewIfNotExists() throws Exception {
        ThemeConfigurationController.Theme themeRequest = new ThemeConfigurationController.Theme();
//        themeRequest.setBranchKey("new-branch");
//        themeRequest.setSignageLogo("newLogo.png");
//        themeRequest.setDispenserLogo("dispLogo.png");
//        themeRequest.setPrinterLogo("printerLogo.png");
//        themeRequest.setFeedbackLogo("feedbackLogo.png");
//        themeRequest.setQrcodeLogo("qrcodeLogo.png");
//        themeRequest.setPrimaryColor("#000000");
//        themeRequest.setSecondaryColor("#111111");
//        themeRequest.setPrimaryTextColor("#222222");
//        themeRequest.setSecondaryTextColor("#333333");
//        themeRequest.setQrPrimaryColor("#444444");
//        themeRequest.setQrSecondaryColor("#555555");
//        themeRequest.setBackgroundImage("bg.png");
//        themeRequest.setShowTime(true);
//        themeRequest.setLoginScreenLogo("loginLogo.png");

        Mockito.when(themeConfigurationRepository.findByBranchKey("new-branch"))
                .thenReturn(Optional.empty());

        Mockito.when(themeConfigurationRepository.save(Mockito.any(ThemeConfiguration.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/api/config/theme")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(themeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchKey").value("new-branch"))
                .andExpect(jsonPath("$.signageLogo").value("newLogo.png"))
                .andExpect(jsonPath("$.showTime").value(false)); // Because constructor uses false for showTime
    }

    @Test
    void testEditTheme_updatesExisting() throws Exception {
        ThemeConfiguration existingConfig = new ThemeConfiguration();
        existingConfig.setBranchKey("branch-exist");
        existingConfig.setSignageLogo("oldLogo.png");

        ThemeConfigurationController.Theme themeRequest = new ThemeConfigurationController.Theme();
//        themeRequest.setBranchKey("branch-exist");
//        themeRequest.setSignageLogo("updatedLogo.png");

        Mockito.when(themeConfigurationRepository.findByBranchKey("branch-exist"))
                .thenReturn(Optional.of(existingConfig));

        Mockito.when(themeConfigurationRepository.save(Mockito.any(ThemeConfiguration.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/api/config/theme")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(themeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchKey").value("branch-exist"))
                .andExpect(jsonPath("$.signageLogo").value("updatedLogo.png"));
    }
}
