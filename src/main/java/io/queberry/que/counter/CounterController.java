package io.queberry.que.counter;

import io.queberry.que.dto.ServiceList;
import io.queberry.que.branch.Branch;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class CounterController {

    private final CounterService counterService;

    public CounterController(CounterService counterService) {
        this.counterService = counterService;
    }

    @GetMapping("/counters/active/{id}")
    public ResponseEntity<Set<Counter>> activeFindAll(@PathVariable("id") Branch branch) {
        return ResponseEntity.ok(counterService.activeFindAll(branch));
    }

    @PutMapping("/counters/{id}/activate")
    public ResponseEntity<Counter> activate(@PathVariable("id") Counter counter) {
        return ResponseEntity.ok(counterService.activate(counter));
    }

    @PutMapping("/counters/{id}/deactivate")
    public ResponseEntity<?> deactivate(@PathVariable("id") String counterId) {
        return ResponseEntity.ok(counterService.deactivate(counterId));
    }

    @PostMapping("/counters")
    public ResponseEntity<?> save(@RequestBody CounterResources resource) {
        return ResponseEntity.ok(counterService.save(resource));
    }

    @PutMapping("/counters/{id}")
    public ResponseEntity<?> editCounter(@PathVariable("id") String counterId,
                                         @RequestBody CounterResources resource) {
        try {
            Counter updatedCounter = counterService.editCounter(counterId, resource);
            return ResponseEntity.ok("Counter Updated Successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }
    @PutMapping("/counters/{id}/services/add")
    public ResponseEntity<?> addServicesToCounter(@PathVariable("id") Counter counter,
                                                  @RequestBody ServiceList serviceList) {
        if (counter == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Counter ID doesn't exist!");
        }
        if (serviceList.getFirst() == null || serviceList.getFirst().isEmpty()) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body("At least one First Level Service is required.");
        }
        try {
            Counter updatedCounter = counterService.addServices(counter, serviceList);
            return ResponseEntity.ok("Counter Updated Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add services: " + e.getMessage());
        }
    }
    @GetMapping("/counters/unassigned/{branchId}")
    public ResponseEntity<?> listUnassignedCounters(@PathVariable("branchId") String branchId) {
        Set<Counter> counters = counterService.listUnassignedCounters(branchId);
        return ResponseEntity.ok(counters);
    }
    @GetMapping("/counters/{id}/free")
    public ResponseEntity<?> inUse(@PathVariable("id") String ctr) {
        return ResponseEntity.ok(counterService.inUse(ctr));
    }
    @PutMapping("/counters/{id}/disable")
    public ResponseEntity<?> disable(HttpServletRequest request, @PathVariable("id") String counterId) {
        Counter updatedCounter = counterService.disableCounter(request, counterId);
        return ResponseEntity.ok(updatedCounter);
    }
    @PutMapping("/counters/{id}/enable")
    public ResponseEntity<?> enableCounter(HttpServletRequest request, @PathVariable("id") String counterId) {
        Counter updatedCounter = counterService.enableCounter(request, counterId);
        return ResponseEntity.ok(updatedCounter);
    }
    @GetMapping("/counters")
    public ResponseEntity<?> getCounters(Pageable pageable) {
        Page<Counter> countersList = counterService.getCounters(pageable);
        return ResponseEntity.ok(countersList);
    }
    @GetMapping("/counters/{id}/filterByCode")
    public ResponseEntity<?> filterCounterByCode(
            @PathVariable("id") String branchId,
            @RequestParam("counterId") String cd,
            Pageable pageable) {

        Page<Counter> counters = counterService.filterCountersByCode(branchId, cd, pageable);
        return ResponseEntity.ok(counters);
    }
//    @Transaction
    @GetMapping("/counters/{id}/exit/{empId}")
    public ResponseEntity<?> exit(@PathVariable("id") String counterId, @PathVariable("empId") String empId) {
        Counter updatedCounter = counterService.exitCounter(counterId, empId);
        return ResponseEntity.ok(updatedCounter);
    }
}
