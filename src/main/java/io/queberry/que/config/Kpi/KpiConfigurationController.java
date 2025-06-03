package io.queberry.que.config.Kpi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class KpiConfigurationController {

    private final KpiConfigurationRepository kpiConfigurationRepository;

    public KpiConfigurationController(KpiConfigurationRepository kpiConfigurationRepository) {
        this.kpiConfigurationRepository = kpiConfigurationRepository;
    }


    @GetMapping("/config/kpi")
    public ResponseEntity getKpiConfig(){
//        log.info("in get mail config");
        return ResponseEntity.ok(kpiConfigurationRepository.findAll().stream().findFirst().orElse(new KpiConfiguration()));
    }

    @PutMapping("/config/kpi")
    public ResponseEntity editkpiConfig(@RequestBody KpiConfiguration resource){
        KpiConfiguration kpiConfiguration = kpiConfigurationRepository.findAll().stream().findFirst().orElse(new KpiConfiguration());
        kpiConfiguration.change(resource);
        kpiConfiguration = kpiConfigurationRepository.save(kpiConfiguration);
        return ResponseEntity.ok(kpiConfiguration);
    }

}
