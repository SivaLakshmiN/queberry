package io.queberry.que.Controller;
import io.queberry.que.Entity.*;
import io.queberry.que.Repository.BreakConfigurationRepository;
import io.queberry.que.Repository.BreakSessionRepository;
import io.queberry.que.Repository.CounterRepository;
import io.queberry.que.Repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@CloudloomController
@RequiredArgsConstructor
public class BreakController {

    private final BreakConfigurationRepository breakConfigurationRepository;

    private final BreakSessionRepository breakSessionRepository;

    private final EmployeeRepository employeeRepository;
//    private final WebSocketOperations messagingTemplate;
    private final CounterRepository counterRepository;
//    private final CounterStatusService counterStatusService;

    @GetMapping("/config/break")
    public ResponseEntity getBreakInfo(){
        List<BreakConfiguration> breakConfiguration = breakConfigurationRepository.findAll();
        log.info(breakConfiguration + " breakConfiguration");
        return ResponseEntity.ok(breakConfiguration);
    }

    @RequestMapping(value = "/config/break/create", method = RequestMethod.POST)
    public ResponseEntity createBreak(@RequestBody BreakConfiguration breakConfiguration){

        BreakConfiguration breakConfiguration1 = breakConfigurationRepository.save(breakConfiguration);
        return ResponseEntity.ok(breakConfiguration1);
    }

    @PostMapping("/config/break/edit/{id}")
    public ResponseEntity editBreak(@RequestBody BreakConfiguration breakConfiguration, @PathVariable("id") BreakConfiguration breakConfiguration1 ){
//        BreakConfiguration breakConfiguration1 = breakConfigurationRepository.findById(id).get();
        breakConfiguration1.setBreakName(breakConfiguration.getBreakName());
        breakConfiguration1.setBreakDescription(breakConfiguration.getBreakDescription());
        breakConfiguration1.setApprovalRequest(breakConfiguration.getApprovalRequest());
        breakConfiguration1.setIfExceeds(breakConfiguration.isIfExceeds());
        breakConfiguration1.setDuration(breakConfiguration.getDuration());
        breakConfigurationRepository.save(breakConfiguration1);
        return ResponseEntity.ok(breakConfiguration1);
    }

//    @PutMapping("/break/takeBreak")
//    public ResponseEntity<?> takeBreak(@RequestBody BreakSessionRequest breakSessionRequest){
//        BreakSession breakSession1 = new BreakSession();
//        Optional<Employee> emp = employeeRepository.findEmployeeById(breakSessionRequest.getEmployeeId());
//        breakSession1.setStartTime(LocalDateTime.now());
//        breakSession1.setBreakConfiguration(breakSessionRequest.getBreakConfiguration());
//        breakSession1.setEmployee(emp.get());
//        breakSession1 = breakSessionRepository.save(breakSession1);
//        Optional<Counter> counter = counterRepository.findCounterById(breakSessionRequest.getCounterId());
//        if(counter.isPresent()){
//            Counter c = counter.get();
//            messagingTemplate.send("/notifications/" + c.getBranch().getBranchKey() + "/counterDisplayMessage", new CounterStatusWrapper(c,"BREAK"));
//            counterStatusService.setStatus(c.getId(),"BREAK");
//        }
//        return ResponseEntity.ok(breakSession1);
//    }

//    @PutMapping("/break/resumeBreak/{id}")
//    public ResponseEntity<?> resumeBreak(@PathVariable String id){
//      Optional<BreakSession> bs = breakSessionRepository.findById(id);
//      if(bs.isPresent()){
//          BreakSession breakSession = bs.get();
//          breakSession.setEndTime(LocalDateTime.now());
//          breakSessionRepository.save(breakSession);
//          return ResponseEntity.status(HttpStatus.OK).body(breakSession);
//      }else {
//          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data not found");
//      }
//    }

//    @PutMapping("/break/resumeBreak")
//    public ResponseEntity<?> resumeBreak( @RequestBody BreakSessionRequest breakSessionRequest){
//        Optional<BreakSession> bs = breakSessionRepository.findBreakSessionById(breakSessionRequest.getBreakId());
//        if(bs.isPresent()){
//            bs.get().setEndTime(LocalDateTime.now());
//            breakSessionRepository.save(bs.get());
////            Optional<Employee> emp = employeeRepository.findById(breakSession.getEmployee());
//            Optional<Counter> counter = counterRepository.findCounterById(breakSessionRequest.getCounterId());
//            if(counter.isPresent()){
//                Counter c = counter.get();
//                messagingTemplate.send("/notifications/" + c.getBranch().getBranchKey() + "/counterDisplayMessage", new CounterStatusWrapper(c,"OPEN"));
//                counterStatusService.setStatus(c.getId(), "OPEN");
//            }
//            return ResponseEntity.status(HttpStatus.OK).body(bs);
//        }else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data not found");
//        }
//    }

    @Data
    public static class BreakSessionRequest{
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private BreakConfiguration breakConfiguration;
        private String employeeId;
        private String breakId;
        private String counterId;
    }

    @Data
    @AllArgsConstructor
    public static class CounterStatusWrapper{
        private Counter counter;
        private String ctrStatus;
    }
}
