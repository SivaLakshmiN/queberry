package io.queberry.que.Controller;

import io.queberry.que.Entity.CloudloomController;
import io.queberry.que.Entity.KpiConfiguration;
import io.queberry.que.Repository.KpiConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CloudloomController
@RequiredArgsConstructor
@Slf4j
public class KpiConfigurationController {
    private final KpiConfigurationRepository kpiConfigurationRepository;


    @GetMapping("/config/kpi")
    public ResponseEntity getKpiConfig(){
        log.info("in get mail config");
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
