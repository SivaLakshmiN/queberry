package io.queberry.que.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.queberry.que.branch.BranchController;
import io.queberry.que.dto.ReqBody;
import io.queberry.que.subTransaction.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.http.RequestEntity.put;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BranchController.class)
public class SubTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubTransactionService subTransactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetSubTransactionByRegion() throws Exception {
        String regionId = "123";
        SubTransactionDTO dto = new SubTransactionDTO(); // Set fields if needed
        Page<SubTransactionDTO> page = new PageImpl<>(
                Collections.singletonList(dto),
                PageRequest.of(0, 10),
                1
        );
        Mockito.when(subTransactionService.getSubTransactionsByRegion(eq(regionId), any(Pageable.class)))
                .thenReturn(page);
        mockMvc.perform(get("/sub-transaction/{region_id}", regionId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void testGetAllSubTransactions() throws Exception {
        String regionId = "123";
        SubTransactionDTO dto = new SubTransactionDTO();
        Set<SubTransactionDTO> mockResult = new HashSet<>();
        mockResult.add(dto);

        Mockito.when(subTransactionService.getAllSubTransactionsByRegion(eq(regionId)))
                .thenReturn(mockResult);
        mockMvc.perform(get("/sub-transaction/{region_id}/list", regionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void testGetFilterSubTransaction() throws Exception {
        String regionId = "123";
        String subservice = "billing";
        SubTransactionDTO dto = new SubTransactionDTO();

        Page<SubTransactionDTO> page = new PageImpl<>(
                Collections.singletonList(dto),
                PageRequest.of(0, 10),
                1
        );

        Mockito.when(subTransactionService.getFilterSubTransaction(eq(regionId), eq(subservice), any(Pageable.class)))
                .thenReturn(page);
        mockMvc.perform(get("/sub-transaction/{region_id}/filterByName/{subservice}", regionId, subservice)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void testSaveSubTransaction() throws Exception {
        SubTransaction subTransaction = new SubTransaction();
        subTransaction.setInput(true);
        subTransaction.setName("Test Transaction");

        Mockito.when(subTransactionService.save(Mockito.any(SubTransaction.class)))
                .thenReturn(subTransaction);
        objectMapper.writeValueAsString(subTransaction);
        mockMvc.perform((RequestBuilder) post("/sub-transaction")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void testEditSubTransaction() throws Exception {
        String id = "123";
        SubTransaction input = new SubTransaction();
//        input.setId(123L);
        input.setName("Updated Transaction");

        SubTransaction updated = new SubTransaction();
//        updated.setId(123L);
        updated.setName("Updated Transaction");

        Mockito.when(subTransactionService.edit(eq(id), any(SubTransaction.class))).thenReturn(updated);
        objectMapper.writeValueAsString(input);
        mockMvc.perform((RequestBuilder) put("/sub-transaction/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void testGetSubTransactionGroupByRegion() throws Exception {
        String regionId = "north";

        SubTransactionGroupDTO dto = new SubTransactionGroupDTO();
        dto.setId("g1");
        dto.setName("Group North");

        Page<SubTransactionGroupDTO> page = new PageImpl<>(
                Collections.singletonList(dto),
                PageRequest.of(0, 10),
                1
        );

        Mockito.when(subTransactionService.getSubTransactionGroupsByRegion(eq(regionId), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/sub-transaction-group/{region_id}", regionId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void testGetFilterSubTransactionGroup() throws Exception {
        String regionId = "north";
        String stGroup = "billing";

        SubTransactionGroupDTO dto = new SubTransactionGroupDTO();
        dto.setId("g1");
        dto.setName("Billing Group");

        Page<SubTransactionGroupDTO> page = new PageImpl<>(
                Collections.singletonList(dto),
                PageRequest.of(0, 10),
                1
        );

        Mockito.when(subTransactionService.filterSubTransactionGroupsByRegionAndName(eq(regionId), eq(stGroup), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/sub-transaction-group/{region_id}/filterByName", regionId)
                        .param("stGroup", stGroup)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void testSaveSubTransactionGroup() throws Exception {
        SubTransactionGroupReq req = new SubTransactionGroupReq();
        req.setName("Finance Group");

        SubTransactionGroup saved = new SubTransactionGroup();
//        saved.setActive(1L);
        saved.setName("Finance Group");

        Mockito.when(subTransactionService.saveSubTransactionGroup(any(SubTransactionGroupReq.class)))
                .thenReturn(saved);
        objectMapper.writeValueAsString(req);
        mockMvc.perform((RequestBuilder) post("/sub-transaction-group")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void testEditSubTransactionGroup() throws Exception {
        String id = "123";

        SubTransactionGroupReq editReq = new SubTransactionGroupReq();
        editReq.setName("Updated Group");

        SubTransactionGroup updatedGroup = new SubTransactionGroup();
//        updatedGroup.se(123L);
        updatedGroup.setName("Updated Group");

        Mockito.when(subTransactionService.editSubTransactionGroup(eq(id), any(SubTransactionGroupReq.class)))
                .thenReturn(updatedGroup);
        objectMapper.writeValueAsString(editReq);
        mockMvc.perform((RequestBuilder) put("/sub-transaction-group/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void testAssignSubTransactionGroupToServices() throws Exception {
        String groupId = "group123";
        ReqBody reqBody = new ReqBody();
        reqBody.setServices(Set.of("123","345"));

        Mockito.doNothing().when(subTransactionService)
                .assignSubTransactionGroupToServices(eq(groupId), eq(reqBody.getServices()));
        objectMapper.writeValueAsString(reqBody);
        mockMvc.perform((RequestBuilder) put("/assign-sub-service/{id}", groupId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
