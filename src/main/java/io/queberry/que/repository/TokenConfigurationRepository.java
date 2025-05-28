package io.queberry.que.repository;

import io.queberry.que.config.TokenConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenConfigurationRepository extends JpaRepository<TokenConfiguration,String> {
}
