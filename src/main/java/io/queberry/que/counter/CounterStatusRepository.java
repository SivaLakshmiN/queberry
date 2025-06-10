package io.queberry.que.counter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CounterStatusRepository extends JpaRepository<CounterStatus, String> {

    Optional<CounterStatus> findByCounter(String counter);
    CounterStatus deleteByCounter(String counter);
}
