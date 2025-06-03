package io.queberry.que.config.Salesforce;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesforceConfigurationRepository extends JpaRepository<SalesforceConfiguration,String> {
}
