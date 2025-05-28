package io.queberry.que.Controller;
import io.queberry.que.Entity.CloudloomController;
import io.queberry.que.Entity.SurveyConfiguration;
import io.queberry.que.Repository.SurveyConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CloudloomController
@RequiredArgsConstructor
public class SurveyConfigurationController {

    private final SurveyConfigurationRepository surveyConfigurationRepository;

    @GetMapping("/config/survey")
    public ResponseEntity getSmsConfig(){
        return ResponseEntity.ok(surveyConfigurationRepository.findAll().stream().findFirst().orElse(new SurveyConfiguration()));
    }

    @PutMapping("/config/survey")
    public ResponseEntity editSmsConfig(@RequestBody SurveyConfiguration resource){
        SurveyConfiguration surveyConfiguration = surveyConfigurationRepository.findAll().stream().findFirst().orElse(new SurveyConfiguration());
        surveyConfiguration.change(resource);
        surveyConfiguration = surveyConfigurationRepository.save(surveyConfiguration);
        return ResponseEntity.ok(surveyConfiguration);
    }

}
