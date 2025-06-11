package io.queberry.que.failed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface FailedLoginRepository extends JpaRepository<FailedLogin, String> {
    FailedLogin findByEmployeeId(String employee);

    @Query("SELECT COUNT(f) FROM FailedLogin f WHERE f.employeeId = :employee AND f.attemptTime >= :attemptTime")
    int countFailedAttempts(@Param("employee") String employee, @Param("attemptTime") LocalDateTime attemptTime);

}
