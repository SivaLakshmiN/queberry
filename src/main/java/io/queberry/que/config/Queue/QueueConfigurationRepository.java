package io.queberry.que.config.Queue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QueueConfigurationRepository extends JpaRepository<QueueConfiguration,String> {
    Optional<QueueConfiguration> findByBranchKey(String name);
}
