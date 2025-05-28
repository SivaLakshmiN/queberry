package io.queberry.que.Repository;

import io.queberry.que.Entity.MeetingConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingConfigurationRepository extends JpaRepository<MeetingConfiguration,String> {
}
