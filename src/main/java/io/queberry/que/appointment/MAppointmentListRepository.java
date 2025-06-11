package io.queberry.que.appointment;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface MAppointmentListRepository extends JpaRepository<MAppointmentList,String> {

    Set<MAppointmentList> findByMobile(String mobile, Sort sort);
}

