package io.queberry.que.Controller;
import io.queberry.que.Entity.CloudloomController;
import io.queberry.que.Entity.QueueConfiguration;
import io.queberry.que.Exception.QueueException;
import io.queberry.que.Repository.QueueConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@CloudloomController
@RequiredArgsConstructor
public class QueueConfigurationController {

    private final QueueConfigurationRepository queueConfigurationRepository;

    @GetMapping("/config/queue/strategies")
    public ResponseEntity getAllStrategies(){
        return ResponseEntity.ok(QueueConfiguration.QueueStrategy.values());
    }

    @GetMapping("/config/queue/{branchKey}")
    public ResponseEntity getQueueConfig(@PathVariable("branchKey") String branchKey){
        Optional<QueueConfiguration> queueConfiguration = queueConfigurationRepository.findByBranchKey(branchKey);
        if (queueConfiguration.isPresent())
            return ResponseEntity.ok(queueConfiguration);
        else
           throw new QueueException("No Queue Configuration Found",HttpStatus.PRECONDITION_FAILED);
    }


//    @PutMapping("/config/queue")
//    public ResponseEntity editQueueConfig(@RequestParam("strategy") QueueConfiguration.QueueStrategy strategy,@RequestParam("slaWait") Integer slaWait,@RequestParam("slaServe")Integer slaServe, @RequestParam("servicePriority") QueueConfiguration.ServicePriority servicePriority){
//        QueueConfiguration queueConfiguration = queueConfigurationRepository.findAll().stream().findFirst().orElse(null);
//        if (queueConfiguration == null)
//            throw new QueueException("No Queue Configuration Found",HttpStatus.PRECONDITION_FAILED);
//        queueConfiguration.change(strategy,slaWait,slaServe, servicePriority);
//        return ResponseEntity.ok(queueConfigurationRepository.save(queueConfiguration));
//    }

    @PutMapping("/config/queue")
    public ResponseEntity<QueueConfiguration> editQueueConfig(@RequestBody QueueConfiguration queueConfiguration){
        QueueConfiguration queueConfiguration1 = queueConfigurationRepository.findByBranchKey(queueConfiguration.getBranchKey()).orElse(null);
        if(queueConfiguration1 != null){
            log.info("in if");
            queueConfiguration1.setServicePriority(queueConfiguration.getServicePriority());
            queueConfiguration1.setSlaServe(queueConfiguration.getSlaServe());
            queueConfiguration1.setSlaWait(queueConfiguration.getSlaWait());
            queueConfiguration1.setStrategy(queueConfiguration.getStrategy());
            return ResponseEntity.ok(queueConfigurationRepository.save(queueConfiguration1));
        }else{
            log.info("in else");
            return ResponseEntity.ok(queueConfigurationRepository.save(queueConfiguration));
        }
    }

    @PostMapping("/config/queue")
    public ResponseEntity<QueueConfiguration> saveQueueConfig(@RequestBody QueueConfiguration queueConfiguration){
        return ResponseEntity.ok(queueConfigurationRepository.save(queueConfiguration));
    }

}
