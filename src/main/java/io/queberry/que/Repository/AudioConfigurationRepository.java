package io.queberry.que.Repository;

import io.queberry.que.Entity.AudioConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface AudioConfigurationRepository extends JpaRepository<AudioConfiguration,String> {
    Optional<AudioConfiguration> findByBranchKey(String name);
}
