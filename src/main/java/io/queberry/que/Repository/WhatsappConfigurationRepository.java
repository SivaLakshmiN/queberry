package io.queberry.que.Repository;

import io.queberry.que.Entity.WhatsappConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface WhatsappConfigurationRepository extends JpaRepository<WhatsappConfiguration,String> {
    Optional<WhatsappConfiguration> findByTenant(String tenant);
    Optional<WhatsappConfiguration> findByMobile(String mobile);
}
