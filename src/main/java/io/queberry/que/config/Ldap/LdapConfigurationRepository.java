package io.queberry.que.config.Ldap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LdapConfigurationRepository extends JpaRepository<LdapConfiguration,String> {
}
