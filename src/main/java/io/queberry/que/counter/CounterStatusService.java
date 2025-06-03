//package io.queberry.que.service;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Slf4j
//@Service
//public class CounterStatusService {
//
//    @Autowired
//    private CounterStatusRepository counterStatusRepository;
//
//    public void setStatus(String counter, String status){
//        CounterStatus counterStatus = counterStatusRepository.findByCounter(counter).orElse(null);
//        if(counterStatus != null){
//            counterStatus.setStatus(status);
//            counterStatusRepository.save(counterStatus);
//        }else{
//            if(status.equals("CLOSED")){
//                counterStatusRepository.deleteByCounter(counter);
//            }
//            CounterStatus cs = new CounterStatus(counter, status);
//            counterStatusRepository.save(cs);
//        }
//    }
//
//    public String getStatus(String counter){
//        CounterStatus counterStatus = counterStatusRepository.findByCounter(counter).orElse(null);
//        if(counterStatus != null){
//            log.info("{}",counterStatus.getStatus());
//            return counterStatus.getStatus();
//        }else{
//            return "CLOSED";
//        }
//    }
//}
//
