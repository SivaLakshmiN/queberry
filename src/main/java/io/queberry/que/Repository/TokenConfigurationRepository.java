package io.queberry.que.Repository;

import io.queberry.que.Entity.TokenConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TokenConfigurationRepository extends JpaRepository<TokenConfiguration,String> {
}
