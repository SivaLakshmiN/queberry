package io.queberry.que.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.queberry.que.counter.Counter;
import io.queberry.que.counter.CounterController;
import io.queberry.que.counter.CounterResources;
import io.queberry.que.counter.CounterService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.anything;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CounterController.class)
public class CounterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CounterService counterService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetCounters() throws Exception {
        Page<Counter> page = new PageImpl<>(List.of(new Counter()));
        Mockito.when(counterService.getCounters(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/counters"))
                .andExpect(status().isOk());
    }

    @Test
    void testActivateCounter() throws Exception {
        Counter dummy = new Counter();
        Mockito.when(counterService.activate(any())).thenReturn(dummy);

        mockMvc.perform(put("/counters/123/activate"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeactivateCounter() throws Exception {
        Mockito.when(counterService.deactivate("123")).thenReturn(new Counter());

        mockMvc.perform(put("/counters/123/deactivate"))
                .andExpect(status().isOk());
    }

    @Test
    void testSaveCounter() throws Exception {
        CounterResources resources = new CounterResources();
        Mockito.when(counterService.save(any())).thenReturn(new Counter());
        objectMapper.writeValueAsString(resources);
        mockMvc.perform((RequestBuilder) post("/counters")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testEditCounter_Success() throws Exception {
        CounterResources resource = new CounterResources();
        Mockito.when(counterService.editCounter(Mockito.eq("123"), any())).thenReturn(new Counter());

        mockMvc.perform(put("/counters/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(resource)))
                .andExpect(status().isOk());
    }

    @Test
    void testFilterCounterByCode() throws Exception {
        Page<Counter> counters = new PageImpl<>(List.of(new Counter()));
        Mockito.when(counterService.filterCountersByCode("b1", "c1", Pageable.unpaged())).thenReturn(counters);

        mockMvc.perform(get("/counters/b1/filterByCode?counterId=c1"))
                .andExpect(status().isOk());
    }

    @Test
    void testListUnassignedCounters() throws Exception {
        Mockito.when(counterService.listUnassignedCounters("b1")).thenReturn(Set.of(new Counter()));

        mockMvc.perform(get("/counters/unassigned/b1"))
                .andExpect(status().isOk());
    }

    @Test
    void testInUse() throws Exception {
        Mockito.when(counterService.inUse("counter123")).thenReturn(any());

        mockMvc.perform(get("/counters/counter123/free"))
                .andExpect(status().isOk());
    }

    @Test
    void testDisableCounter() throws Exception {
        Counter dummy = new Counter();
        dummy.setName("counter123");

        Mockito.when(counterService.disableCounter(any(), Mockito.eq("counter123"))).thenReturn(dummy);

        mockMvc.perform(put("/counters/counter123/disable"))
                .andExpect(status().isOk());
    }

    @Test
    void testEnableCounter() throws Exception {
        Counter dummy = new Counter();

        Mockito.when(counterService.enableCounter(any(), Mockito.eq("counter123"))).thenReturn(dummy);

        mockMvc.perform(put("/counters/counter123/enable"))
                .andExpect(status().isOk());
    }

    @Test
    void testExitCounter() throws Exception {
        Counter dummy = new Counter();
//        dummy.setId("counter123");
        dummy.setName("name");
        Mockito.when(counterService.exitCounter("counter123", "emp001")).thenReturn(dummy);

        mockMvc.perform(get("/counters/counter123/exit/emp001"))
                .andExpect(status().isOk());
    }
}
