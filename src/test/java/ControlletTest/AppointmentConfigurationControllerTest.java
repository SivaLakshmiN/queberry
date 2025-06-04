package ControlletTest;

import io.queberry.que.config.Appointment.AppointmentConfiguration;
import io.queberry.que.config.Appointment.AppointmentConfigurationController;
import io.queberry.que.config.Appointment.AppointmentConfigurationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AppointmentConfigurationController.class)
class AppointmentConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppointmentConfigurationRepository appointmentConfigurationRepository;

    @Test
    void testGetAppointmentConfig() throws Exception {
        AppointmentConfiguration config = new AppointmentConfiguration(true, true,
                "English SMS", "Secondary SMS", "Edited English", "Cancel English",
                15, "tenant1", "client1", "secret", "user1", "Subject", "Body");

        when(appointmentConfigurationRepository.findAll()).thenReturn(List.of(config));

        mockMvc.perform(get("/api/config/appointment"))
                .andExpect(status().isOk());
    }

    @Test
    void testEditAppointmentConfig() throws Exception {
        AppointmentConfiguration config = new AppointmentConfiguration();
        AppointmentConfigurationController.AppointmentConfigurationResource resource = new AppointmentConfigurationController.AppointmentConfigurationResource();
//        resource.setEnabled(true);
//        resource.setSmsTextEnglish("Updated SMS");
//        resource.setCheckInTime("25");

        when(appointmentConfigurationRepository.findAll()).thenReturn(List.of(config));
        when(appointmentConfigurationRepository.save(any()));

        mockMvc.perform((org.springframework.test.web.servlet.RequestBuilder) put("/api/config/appointment")
                        .contentType(MediaType.APPLICATION_JSON));
//                        .queryParams(new ObjectMapper().writeValueAsString(resource)))
//                .andExpect(status().isOk());
    }

    @Test
    void testConvertToHtml() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "Test DOCX content".getBytes());

        mockMvc.perform(multipart("/api/config/convertHtml").file(mockFile))
                .andExpect(status().is5xxServerError()); // since Docx4J will fail on dummy content
    }
}
