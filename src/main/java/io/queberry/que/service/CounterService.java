package io.queberry.que.service;

import io.queberry.que.dto.CounterResources;
import io.queberry.que.dto.ServiceList;
import io.queberry.que.entity.Branch;
import io.queberry.que.entity.Counter;

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
}
