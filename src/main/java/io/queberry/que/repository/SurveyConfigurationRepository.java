package io.queberry.que.repository;

import io.queberry.que.config.SurveyConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyConfigurationRepository extends JpaRepository<SurveyConfiguration,String> {
//    Optional<SurveyConfiguration> findByBranchKey();
}
