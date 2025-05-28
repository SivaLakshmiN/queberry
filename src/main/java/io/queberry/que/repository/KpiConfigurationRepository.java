package io.queberry.que.repository;

import io.queberry.que.config.KpiConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KpiConfigurationRepository extends JpaRepository<KpiConfiguration, String> {
}