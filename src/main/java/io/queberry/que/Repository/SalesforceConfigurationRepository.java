package io.queberry.que.Repository;

import io.queberry.que.Entity.SalesforceConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface SalesforceConfigurationRepository extends JpaRepository<SalesforceConfiguration,String> {
}
