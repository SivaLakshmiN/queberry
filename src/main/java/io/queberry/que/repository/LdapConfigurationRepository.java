package io.queberry.que.repository;

import io.queberry.que.config.LdapConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LdapConfigurationRepository extends JpaRepository<LdapConfiguration,String> {
}
