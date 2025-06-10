package io.queberry.que.queue;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface AppointmentTokenRepository extends JpaRepository<AppointmentToken,String> {
    List<AppointmentToken> findByServiceIdOrderByAppointmentAtAsc(String serviceId);
    Set<AppointmentToken> findByServiceIdInOrderByAppointmentAtAsc(Set<String> serviceId);
    AppointmentToken findByTokenId(String id);
}
