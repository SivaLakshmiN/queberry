package io.queberry.que.config.Sms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsConfigurationRepository extends JpaRepository<SmsConfiguration, String> {
}