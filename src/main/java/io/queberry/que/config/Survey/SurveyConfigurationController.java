package io.queberry.que.config.Survey;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SurveyConfigurationController {

    private final SurveyConfigurationRepository surveyConfigurationRepository;

    public SurveyConfigurationController(SurveyConfigurationRepository surveyConfigurationRepository) {
        this.surveyConfigurationRepository = surveyConfigurationRepository;
    }

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
