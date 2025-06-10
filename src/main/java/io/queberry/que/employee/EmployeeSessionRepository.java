package io.queberry.que.employee;
import io.queberry.que.assistance.Assistance;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EmployeeSessionRepository extends JpaRepository<EmployeeSessions, String> {
    //    EmployeeSessions findByEmployeeAndLogoutTimeIsNull(Employee e);
    Set<EmployeeSessions> findByEmployeeAndLogoutTimeIsNull(Employee e);
    Set<EmployeeSessions> findByLogoutTimeIsNullAndEmployeeId(String e);
    Set<EmployeeSessions> findByLoginTimeBetweenAndEmployeeIdAndLogoutTimeIsNull(LocalDateTime startOfDay, LocalDateTime endOfDay, String employeeId);


    Set<EmployeeSessions> findByEmployeeAndLoginTimeBetweenOrderByLoginTimeAsc(Employee e, LocalDateTime lsdate, LocalDateTime ledate);
    Set<EmployeeSessions> findByEmployeeIdAndLoginTimeBetweenOrderByLoginTimeAsc(String e, LocalDateTime lsdate, LocalDateTime ledate);

    @Query("SELECT new io.queberry.que.employee.EmployeeSessionsDTO(a) " +
            "FROM que_employee_session a WHERE a.loginTime BETWEEN :from AND :to AND a.employee.id = :employee")
    List<EmployeeSessionsDTO> findByLoginTimeBetweenAndEmployeeIdOrderByLoginTimeAsc(
            @Param("employee") String employee,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}

