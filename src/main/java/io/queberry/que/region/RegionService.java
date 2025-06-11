package io.queberry.que.region;

import io.queberry.que.appointment.AppointmentRepository;
import io.queberry.que.assistance.AssistanceRepository;
import io.queberry.que.branch.Branch;
import io.queberry.que.branch.BranchRepository;
import io.queberry.que.config.Redis.RedisSequenceEngine;
import io.queberry.que.config.Websocket.WebSocketOperations;
import io.queberry.que.exception.QueueException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class RegionService {

    private final RegionRepository regionRepository;

    private final BranchRepository branchRepository;

    private final AssistanceRepository assistanceRepository;

    private final AppointmentRepository appointmentRepository;

    private final WebSocketOperations messagingTemplate;

    private final RedisSequenceEngine sequenceEngine;

//    public ResponseEntity<?> editRegion(String id, Region region){
//        Optional<Region> region1 = regionRepository.findById(id);
//        if(region1.isPresent()){
//            Region r = region1.get();
//            r.setName(region.getName());
//
//            if(r.isActive() && !(region.isActive())) {
//                Set<Branch> branches = branchRepository.findByRegionAndActiveTrue(r);
//                Set<Status> status = new HashSet<>();
//                status.add(Status.EXPIRED);
//                status.add(Status.COMPLETED);
//                status.add(Status.NO_SHOW);
//                Set<String> exceptionList = new HashSet<>();
//                log.info("{}", exceptionList.size());
//                LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
//                LocalDateTime end = LocalDateTime.of(LocalDate.now(),LocalTime.MAX);
//                for (Branch branch : branches) {
//                    Set<Assistance> ass = assistanceRepository.findByBranchAndStatusNotInAndCreatedAtBetween(branch.getBranchKey(), status, start, end);
//                    Set<Appointment> app = appointmentRepository.findByBranch(branch.getBranchKey());
//                    log.info("{}, {}", app.size(), ass.size());
//                    if (ass.size() > 0 || app.size() > 0) {
//                        exceptionList.add(branch.getName());
//                    } else {
//                        disableBranches(branch);
//                    }
//                }
//                log.info("{}", exceptionList.size());
//                if(exceptionList.size() > 0)
//                    return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(exceptionList + " still have open tickets");
//            }else if (!(r.isActive()) && region.isActive()) {
//                enableBranches(r);
//            }
//            r.setActive(region.isActive());
//            r.setEnableBranchGroup(region.isEnableBranchGroup());
//            return ResponseEntity.ok(regionRepository.save(r));
//        }else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No region found");
//        }
//    }


    public void disableBranches(Branch branch){
//        branch.setActive(false);
        branchRepository.save(branch);
        sequenceEngine.removeBranchFromSequence(branch.getBranchKey());
        messagingTemplate.send("/notifications/" + branch.getBranchKey() + "/branch/inactive", branch.getName());
    }

//    public void enableBranches(Region region){
//        Set<Branch> branches = branchRepository.findByRegion(region);
//        for(Branch branch: branches) {
////            branch.setActive(true);
//            branchRepository.save(branch);
//        }
//        sequenceEngine.setSequenceManagerByBranches(branches);
//    }

    public Region getRegionByName(String name) {
        return regionRepository.findById(name).orElseThrow(() -> new QueueException("Region not found", HttpStatus.NOT_FOUND));
    }
}
