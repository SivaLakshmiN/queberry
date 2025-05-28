package io.queberry.que.Repository;

import io.queberry.que.DTO.BreakSessionDTO;
import io.queberry.que.Entity.BreakSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BreakSessionRepository extends JpaRepository<BreakSession, String> {
//    Set<BreakSession> findByEmployeeAndStartTimeBetween(Employee employee, LocalDateTime start, LocalDateTime end);
//    Set<BreakSession> findByEmployeeIdAndStartTimeBetween(String employee, LocalDateTime start, LocalDateTime end);
//    @Query("SELECT new io.queberry.que.configuration.breakConfig.BreakSessionDTO(a) " +
//            "FROM BreakSession a WHERE a.startTime BETWEEN :from AND :to AND a.employee.id = :employee")
//    List<BreakSessionDTO> findByStartTimeBetweenAndEmployeeId(
//            @Param("employee") String employee,
//            @Param("from") LocalDateTime from,
//            @Param("to") LocalDateTime to
//    );
////    Set<BreakSession> findByEmployeeAndStartTimeBetweenAndEndTimeIsNull(Employee employee, LocalDateTime start, LocalDateTime end);
//    Set<BreakSession> findByEmployeeIdAndStartTimeBetweenAndEndTimeIsNull(String employee, LocalDateTime start, LocalDateTime end);
//
//    @Query(value = "SELECT * FROM break_session WHERE id = :id", nativeQuery = true)
//    Optional<BreakSession> findBreakSessionById(@Param("id") String id);
}
