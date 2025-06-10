package io.queberry.que.subTransaction;

import io.queberry.que.dto.ReqBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
@Slf4j
@RestController
@RequestMapping("/api")
public class SubTransactionController {

    private final SubTransactionService subTransactionService;

    public SubTransactionController(SubTransactionService subTransactionService) {
        this.subTransactionService = subTransactionService;
    }

    @GetMapping("/sub-transaction/{region_id}")
    public ResponseEntity<Page<SubTransactionDTO>> getSubTransactionByRegion(
            @PathVariable("region_id") String regionId,
            Pageable pageable) {
        log.info("GET /sub-transaction/{} called with pageable {}", regionId, pageable);
        Page<SubTransactionDTO> result = subTransactionService.getSubTransactionsByRegion(regionId, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/sub-transaction/{region_id}/list")
    public ResponseEntity<Set<SubTransactionDTO>> getAllSubTransactions(@PathVariable("region_id") String regionId) {
        log.info("GET /sub-transaction/{}/list called", regionId);
        Set<SubTransactionDTO> result = subTransactionService.getAllSubTransactionsByRegion(regionId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/sub-transaction/{region_id}/filterByName")
    public ResponseEntity<Page<SubTransactionDTO>> getFilterSubTransaction(
            @PathVariable("region_id") String region,
            @PathVariable("subservice") String subservice,
            Pageable pageable) {
        log.info("GET /sub-transaction/{}/filterByName called with subservice '{}' and pageable {}", region, subservice, pageable);
        Page<SubTransactionDTO> result = subTransactionService.getFilterSubTransaction(region, subservice, pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/sub-transaction")
    public ResponseEntity<?> save(@RequestBody SubTransaction subTransaction) {
        log.info("POST /sub-transaction called with body {}", subTransaction);
        subTransaction = subTransactionService.save(subTransaction);
        return ResponseEntity.ok("Saved Successfully");
    }

    @PutMapping("/sub-transaction/{id}")
    public ResponseEntity<?> edit(@PathVariable("id") String id, @RequestBody SubTransaction editSubTransaction) {
        log.info("PUT /sub-transaction/{} called with body {}", id, editSubTransaction);
        SubTransaction updated = subTransactionService.edit(id, editSubTransaction);
        return ResponseEntity.ok("Update SubTransaction Successfully");
    }

    @GetMapping("/sub-transaction-group/{region_id}")
    public ResponseEntity<?> getSubTransactionGroupByRegion(@PathVariable("region_id") String region, Pageable pageable) {
        log.info("GET /sub-transaction-group/{} called with pageable {}", region, pageable);
        Page<SubTransactionGroupDTO> response = subTransactionService.getSubTransactionGroupsByRegion(region, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sub-transaction-group/{region_id}/filterByName")
    public ResponseEntity<?> getFilterSubTransactionGroup(
            @PathVariable("region_id") String region,
            @RequestParam("stGroup") String stGroup,
            Pageable pageable) {
        log.info("GET /sub-transaction-group/{}/filterByName called with stGroup '{}' and pageable {}", region, stGroup, pageable);
        Page<SubTransactionGroupDTO> response =
                subTransactionService.filterSubTransactionGroupsByRegionAndName(region, stGroup, pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sub-transaction-group")
    private ResponseEntity<?> saveSubTransactionGroup(@RequestBody SubTransactionGroupReq stgReq) {
        log.info("POST /sub-transaction-group called with body {}", stgReq);
        SubTransactionGroup savedGroup = subTransactionService.saveSubTransactionGroup(stgReq);
        return ResponseEntity.ok("Saved Successfully");
    }

    @PutMapping("/sub-transaction-group/{id}")
    private ResponseEntity<?> editSubTransactionGroup(@PathVariable("id") String id,
                                                      @RequestBody SubTransactionGroupReq editReq) {
        log.info("PUT /sub-transaction-group/{} called with body {}", id, editReq);
        SubTransactionGroup updated = subTransactionService.editSubTransactionGroup(id, editReq);
        return ResponseEntity.ok("Update SubTransaction Successfully");
    }

    @PutMapping("/assign-sub-service/{id}")
    private ResponseEntity<?> assignSubTransactionGroupToServices(@PathVariable("id") String groupId,
                                                                  @RequestBody ReqBody reqBody) {
        log.info("PUT /assign-sub-service/{} called with services {}", groupId, reqBody.getServices());
        subTransactionService.assignSubTransactionGroupToServices(groupId, reqBody.getServices());
        return ResponseEntity.ok("Update SubTransaction Successfully");
    }
}