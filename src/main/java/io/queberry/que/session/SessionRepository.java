package io.queberry.que.session;

import io.queberry.que.service.Service;
import io.queberry.que.service.ServiceDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Set;

public interface SessionRepository extends JpaRepository<Session,String> {
    Set<Session> findByEmployeeAndCreatedAtBetween(String empId, LocalDateTime start, LocalDateTime end);
    Set<Session> findByEmployeeAndServiceInAndCreatedAtBetween(String empId, Set<Service> serviceList, LocalDateTime start, LocalDateTime end);
    Set<Session> findByEmployeeAndServiceIdInAndCreatedAtBetween(String empId, Set<ServiceDTO> serviceList, LocalDateTime start, LocalDateTime end);
}
