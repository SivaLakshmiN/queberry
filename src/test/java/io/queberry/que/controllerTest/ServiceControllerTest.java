package io.queberry.que.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.queberry.que.dto.ApptServiceResource;
import io.queberry.que.dto.ServiceResource;
import io.queberry.que.dto.ServiceResponse;
import io.queberry.que.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.List;
import java.util.Set;

import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ServiceController.class)
class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServiceService serviceService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetActiveServices() throws Exception {
        Set<Service> mockServices = Set.of(new Service());
        Mockito.when(serviceService.getAllActiveServices(true)).thenReturn(mockServices);

        mockMvc.perform(get("/services/active?active=true"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetRegionActiveServices() throws Exception {
        Set<ServiceRegionResponse> mockResponse = Set.of(new ServiceRegionResponse());
        Mockito.when(serviceService.getRegionActiveServices("region1")).thenReturn(mockResponse);

        mockMvc.perform(get("/services/region1/active"))
                .andExpect(status().isOk());
    }

    @Test
    void testActivateService() throws Exception {
        Service service = new Service();
        service.setActive(true);
        Mockito.when(serviceService.activate("region1")).thenReturn(service);

        mockMvc.perform(put("/services/region1/activate"))
                .andExpect(status().isOk());
    }

    @Test
    void testActivateServiceNoContent() throws Exception {
        Service service = new Service();
        service.setActive(false);
        Mockito.when(serviceService.activate("region1")).thenReturn(service);

        mockMvc.perform(put("/services/region1/activate"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeactivateService() throws Exception {
        Mockito.doNothing().when(serviceService).deactivate("s123");

        mockMvc.perform(put("/services/s123/deactivate"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateServiceSuccess() throws Exception {
        ServiceResource resource = new ServiceResource();
        Service updated = new Service();

        Mockito.when(serviceService.updateService(Mockito.eq("s1"), Mockito.any()))
                .thenReturn(updated);

        mockMvc.perform(put("/services/s1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateServiceNotFound() throws Exception {
        ServiceResource resource = new ServiceResource();

        Mockito.when(serviceService.updateService(Mockito.eq("invalid"), Mockito.any()))
                .thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(put("/services/invalid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isNotFound());
    }
    @Test
    void testAddSubServices() throws Exception {
        Mockito.when(serviceService.subServices(Mockito.eq("s1"), Mockito.any()))
                .thenReturn(new Service());

        mockMvc.perform(put("/services/s1/subservices/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Set.of("sub1", "sub2"))))
                .andExpect(status().isOk());
    }

    @Test
    void testDeactivateSubService() throws Exception {
        Mockito.when(serviceService.deactivateSubServices("s1", "sub1"))
                .thenReturn(new Service());

        mockMvc.perform(put("/services/s1/subServices/deactivate/sub1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllRegionServices() throws Exception {
        Mockito.when(serviceService.getAllRegionServices(Mockito.eq("region1"), Mockito.any()))
                .thenReturn(new Service());

        mockMvc.perform(get("/services/region1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllServices() throws Exception {
        Mockito.when(serviceService.getAllServices()).thenReturn(List.of(new ServiceDTO()));

        mockMvc.perform(get("/services"))
                .andExpect(status().isOk());
    }

    @Test
    void testFilterByName() throws Exception {
        Page<Service> services = new PageImpl<>(List.of(new Service()));
        Mockito.when(serviceService.filterByName("region1", "ServiceX", Pageable.unpaged()))
                .thenReturn(services);

        mockMvc.perform(get("/services/region1/filterByName?service=ServiceX"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindBySubTransactionGroup() throws Exception {
        Mockito.when(serviceService.findBySubServiceGroup("group1"))
                .thenReturn(Set.of(new ServiceDTO()));

        mockMvc.perform(get("/services-by-sub-trans-group/group1"))
                .andExpect(status().isOk());
    }

    @Test
    void testSaveService() throws Exception {
        Service service = new Service();
        Mockito.when(serviceService.createService(Mockito.any(), Mockito.any()))
                .thenReturn(service);
        objectMapper.writeValueAsString(service);
        mockMvc.perform((RequestBuilder) post("/services")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetBranchServicesSuccess() throws Exception {
        Mockito.when(serviceService.getBranchServices(Mockito.any()))
                .thenReturn(Set.of(new ServiceResponse()));

        mockMvc.perform(get("/services/appointmentServices/branch123"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetBranchServicesFailure() throws Exception {
        Mockito.when(serviceService.getBranchServices(Mockito.any()))
                .thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(get("/services/appointmentServices/branch123"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetBranchService() throws Exception {
        ApptServiceResource resource = new ApptServiceResource();
        Mockito.when(serviceService.getBranchService(Mockito.any()))
                .thenReturn(Set.of(new ServiceResponse()));

        mockMvc.perform(put("/services/appointmentServices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetBranchServiceFailure() throws Exception {
        ApptServiceResource resource = new ApptServiceResource();
        Mockito.when(serviceService.getBranchService(Mockito.any()))
                .thenThrow(new RuntimeException("some error"));

        mockMvc.perform(put("/services/appointmentServices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isInternalServerError());
    }
}
