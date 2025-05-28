package io.queberry.que.repository;

import io.queberry.que.config.AudioConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AudioConfigurationRepository extends JpaRepository<AudioConfiguration,String> {
    Optional<AudioConfiguration> findByBranchKey(String name);
}
