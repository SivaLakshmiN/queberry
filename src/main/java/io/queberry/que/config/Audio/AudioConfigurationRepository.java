package io.queberry.que.config.Audio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AudioConfigurationRepository extends JpaRepository<AudioConfiguration,String> {
    Optional<AudioConfiguration> findByBranchKey(String name);
}
