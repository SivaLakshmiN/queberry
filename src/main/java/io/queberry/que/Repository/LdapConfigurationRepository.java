package io.queberry.que.Repository;

import io.queberry.que.Entity.LdapConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(exported = false)
public interface LdapConfigurationRepository extends JpaRepository<LdapConfiguration,String> {
}
