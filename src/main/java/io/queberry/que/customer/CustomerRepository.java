package io.queberry.que.customer;

import io.queberry.que.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    Customer findByUsername(String username);
//    Optional<Customer> findByMobile(String mobile);

    @Query("SELECT e FROM customer e WHERE e.mobile = :mobile")
    Customer findByMobile(@Param("mobile") String mobile);
}
