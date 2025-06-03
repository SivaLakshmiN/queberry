package io.queberry.que.config.Survey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyConfigurationRepository extends JpaRepository<SurveyConfiguration,String> {
//    Optional<SurveyConfiguration> findByBranchKey();
}
