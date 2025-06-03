package io.queberry.que.config.Token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenConfigurationRepository extends JpaRepository<TokenConfiguration,String> {
}
