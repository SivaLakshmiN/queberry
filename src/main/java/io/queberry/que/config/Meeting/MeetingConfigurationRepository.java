package io.queberry.que.config.Meeting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingConfigurationRepository extends JpaRepository<MeetingConfiguration,String> {
}
