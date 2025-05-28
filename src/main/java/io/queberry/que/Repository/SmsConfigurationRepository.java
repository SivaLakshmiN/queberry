package io.queberry.que.Repository;

import io.queberry.que.Entity.SmsConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SmsConfigurationRepository extends JpaRepository<SmsConfiguration, String> {
}