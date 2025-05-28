package io.queberry.que.Repository;

import io.queberry.que.Entity.QueueConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;


@RepositoryRestResource(exported = false)
public interface QueueConfigurationRepository extends JpaRepository<QueueConfiguration,String> {
    Optional<QueueConfiguration> findByBranchKey(String name);
}
