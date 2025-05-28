package io.queberry.que.repository;

import io.queberry.que.config.MeetingConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingConfigurationRepository extends JpaRepository<MeetingConfiguration,String> {
}
