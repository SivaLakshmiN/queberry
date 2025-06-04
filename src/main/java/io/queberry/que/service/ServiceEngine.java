package io.queberry.que.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceEngine {
    private final ServiceRepository serviceRepository;

    @Transactional
    public Service manageSubServices(Service service, Set<String> subServices){
        service.setSubServices(subServices.toString());
        service = serviceRepository.save(service);
        return service;
    }

}
