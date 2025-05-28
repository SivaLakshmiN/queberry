package io.queberry.que.repository;

import io.queberry.que.config.DispenserConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DispenserConfigurationRepository extends JpaRepository<DispenserConfiguration,String> {
    Optional<DispenserConfiguration> findByBranchKey(String name);
}
