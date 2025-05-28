package io.queberry.que.repository;

import io.queberry.que.config.SmsConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsConfigurationRepository extends JpaRepository<SmsConfiguration, String> {
}