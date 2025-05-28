package io.queberry.que.Repository;

import io.queberry.que.Entity.KpiConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KpiConfigurationRepository extends JpaRepository<KpiConfiguration, String> {
}