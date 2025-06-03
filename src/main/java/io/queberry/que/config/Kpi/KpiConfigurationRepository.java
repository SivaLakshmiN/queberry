package io.queberry.que.config.Kpi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KpiConfigurationRepository extends JpaRepository<KpiConfiguration, String> {
}