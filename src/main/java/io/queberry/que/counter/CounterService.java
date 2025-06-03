package io.queberry.que.counter;

import io.queberry.que.dto.ServiceList;
import io.queberry.que.branch.Branch;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface CounterService {

    Set<Counter> activeFindAll(Branch branch);
    Counter activate(Counter counter);
    Counter deactivate(String counterId);
    Counter save(CounterResources resource);
    Counter editCounter(String counterId, CounterResources resource);
    Counter addServices(Counter counter, ServiceList serviceList);
    Set<Counter> listUnassignedCounters(String branchId);
    Counter inUse(String counterId);
    Page<Counter> getCounters(Pageable pageable);
    Counter disableCounter(HttpServletRequest httpServletRequest,String counterId);
    Counter enableCounter(HttpServletRequest request, String counterId);
    Page<Counter> filterCountersByCode(String branchId, String codeFragment, Pageable pageable);
    Counter exitCounter(String counterId, String empId);

}
