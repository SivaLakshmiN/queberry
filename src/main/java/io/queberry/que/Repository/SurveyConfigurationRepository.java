package io.queberry.que.Repository;

import io.queberry.que.Entity.SurveyConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SurveyConfigurationRepository extends JpaRepository<SurveyConfiguration,String> {
//    Optional<SurveyConfiguration> findByBranchKey();
}
