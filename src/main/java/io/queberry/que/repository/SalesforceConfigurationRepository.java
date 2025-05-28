package io.queberry.que.repository;

import io.queberry.que.config.SalesforceConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesforceConfigurationRepository extends JpaRepository<SalesforceConfiguration,String> {
}
