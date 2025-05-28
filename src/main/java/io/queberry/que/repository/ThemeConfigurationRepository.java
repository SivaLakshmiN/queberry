package io.queberry.que.repository;

import io.queberry.que.config.ThemeConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThemeConfigurationRepository extends JpaRepository<ThemeConfiguration,String> {
    Optional<ThemeConfiguration> findByBranchKey(String branchKey);
}
