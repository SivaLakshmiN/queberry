package io.queberry.que.queue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CounterTokenRepository extends JpaRepository<CounterToken,String> {
    List<CounterToken> findByCounterIdOrderByCreatedAtAsc(String counterId);
    List<CounterToken> findByCreatedAtBetween(LocalDateTime s, LocalDateTime e);
    List<CounterToken> findByCounterIdOrEmployeeIdOrderByCreatedAtAsc(String counterId, String employeeId);
    CounterToken findByTokenId(String tokenId);

    void deleteByTokenId(String tokenId);
}
