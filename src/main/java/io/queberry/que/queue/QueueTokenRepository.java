package io.queberry.que.queue;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface QueueTokenRepository extends JpaRepository<QueueToken,String> {
    Set<QueueToken> findByServiceId(String serviceId);
    List<QueueToken> findByServiceIdOrderByCreatedAtAsc(String serviceId);
    List<QueueToken> findByServiceIdAndCreatedAtLessThanEqual(String serviceId, LocalDateTime dateTime);
    Integer countByServiceIdAndCreatedAtLessThan(String serviceId, LocalDateTime dateTime);
    List<QueueToken> findByServiceIdInOrderByCreatedAtAsc(Set<String> serviceIds);
    //    List<QueueToken> findByBranchAndServiceIdInOrderByCreatedAtAsc(String branchKey, Set<String> serviceIds);
    List<QueueToken> findByBranchAndServiceIdInOrderByPriorityAscCreatedAtAsc(String branchKey, Set<String> serviceIds);
    QueueToken findByTokenId(String tokenId);
    List<QueueToken> findByBranchOrderByCreatedAtAsc(String b);
    QueueToken deleteByTokenId(String tokenId);
    List<QueueToken> findByBranch(String branchKey);
    List<QueueToken> findByCreatedAtBetween(LocalDateTime s, LocalDateTime e);
}
