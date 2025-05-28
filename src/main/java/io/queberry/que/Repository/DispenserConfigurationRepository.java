package io.queberry.que.Repository;

import io.queberry.que.Entity.DispenserConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface DispenserConfigurationRepository extends JpaRepository<DispenserConfiguration,String> {
    Optional<DispenserConfiguration> findByBranchKey(String name);
}
