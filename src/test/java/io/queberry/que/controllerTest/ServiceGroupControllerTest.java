package io.queberry.que.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.queberry.que.branch.BranchController;
import io.queberry.que.dto.AddServiceToServiceGroup;
import io.queberry.que.serviceGroup.ServiceGroupRequest;
import io.queberry.que.serviceGroup.ServiceGroup;
import io.queberry.que.serviceGroup.ServiceGroupService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.http.RequestEntity.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BranchController.class)
public class ServiceGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServiceGroupService serviceGroupService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddToServiceGroups() throws Exception {
        String serviceGroupId = "123";
        AddServiceToServiceGroup request = new AddServiceToServiceGroup();
        ServiceGroup mockServiceGroup = new ServiceGroup();
        Mockito.when(serviceGroupService.addServicesToGroup(eq(serviceGroupId), any(AddServiceToServiceGroup.class)))
                .thenReturn(mockServiceGroup);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValueAsString(request);
        mockMvc.perform((RequestBuilder) put("/serviceGroups/{serviceGroup_id}/services", serviceGroupId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
//    @Test
//    void testGetAllServiceGroup() throws Exception {
//        ServiceGroup sg1 = new ServiceGroup();
//        ServiceGroup sg2 = new ServiceGroup();
//        List<ServiceGroup> list = List.of(sg1, sg2);
//        PageImpl<ServiceGroup> page = new PageImpl<>(list);
//        Mockito.when(serviceGroupService.getAllServiceGroups()).thenReturn(page);
//        mockMvc.perform(get("/serviceGroups"))
//                .andExpect(status().isOk());
//    }
    @Test
    void testSaveServiceGroup() throws Exception {
        ServiceGroup inputGroup = new ServiceGroup();
        inputGroup.setName("Test Group");

        ServiceGroup savedGroup = new ServiceGroup();
//        savedGroup.setId("1"); // example ID
        savedGroup.setName("Test Group");
        Mockito.when(serviceGroupService.save(any(ServiceGroup.class))).thenReturn(savedGroup);
        objectMapper.writeValueAsString(inputGroup);
        mockMvc.perform((RequestBuilder) post("/serviceGroups")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Saved Successfully"));
    }
    @Test
    void testEditServiceGroup() throws Exception {
        String serviceGroupId = "123";
        ServiceGroupRequest request = new ServiceGroupRequest();
        request.setName("Updated Name"); // Set required fields
        ServiceGroup updatedGroup = new ServiceGroup();
//        updatedGroup.setId(serviceGroupId);
        updatedGroup.setName("Updated Name");
        Mockito.when(serviceGroupService.edit(eq(serviceGroupId), any(ServiceGroupRequest.class)))
                .thenReturn(updatedGroup);
        objectMapper.writeValueAsString(request);
        mockMvc.perform((RequestBuilder) put("/serviceGroups/{id}", serviceGroupId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Update SubGroup Successfully"));
    }
}
