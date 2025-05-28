package io.queberry.que.Repository;

import io.queberry.que.Entity.ThemeConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface ThemeConfigurationRepository extends JpaRepository<ThemeConfiguration,String> {
    Optional<ThemeConfiguration> findByBranchKey(String branchKey);
}
