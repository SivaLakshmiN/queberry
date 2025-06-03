package io.queberry.que.config.Whatsapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface WhatsappConfigurationRepository extends JpaRepository<WhatsappConfiguration,String> {
    Optional<WhatsappConfiguration> findByTenant(String tenant);
    Optional<WhatsappConfiguration> findByMobile(String mobile);
}


