package io.queberry.que.SubTransaction;

import io.queberry.que.dto.ReqBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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

        Page<SubTransactionDTO> result = subTransactionService.getSubTransactionsByRegion(regionId, pageable);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/sub-transaction/{region_id}/list")
    public ResponseEntity<Set<SubTransactionDTO>> getAllSubTransactions(@PathVariable("region_id") String regionId) {
        Set<SubTransactionDTO> result = subTransactionService.getAllSubTransactionsByRegion(regionId);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/sub-transaction/{region_id}/filterByName")
    public ResponseEntity<Page<SubTransactionDTO>> getFilterSubTransaction(@PathVariable("region_id") String region,
                                                     @PathVariable("subservice") String subservice,
                                                     Pageable pageable) {
        Page<SubTransactionDTO> result = subTransactionService.getFilterSubTransaction(region, subservice, pageable);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/sub-transaction")
    public ResponseEntity<?> save(@RequestBody SubTransaction subTransaction) {
        subTransaction = subTransactionService.save(subTransaction);
        return ResponseEntity.ok("Saved Successfully");
    }
    @PutMapping("/sub-transaction/{id}")
    public ResponseEntity<?> edit(@PathVariable("id") String id, @RequestBody SubTransaction editSubTransaction) {
        SubTransaction updated = subTransactionService.edit(id, editSubTransaction);
        return ResponseEntity.ok("Update SubTransaction Successfully");
    }
    @GetMapping("/sub-transaction-group/{region_id}")
    public ResponseEntity<?> getSubTransactionGroupByRegion(@PathVariable("region_id") String region, Pageable pageable) {
        Page<SubTransactionGroupDTO> response = subTransactionService.getSubTransactionGroupsByRegion(region, pageable);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/sub-transaction-group/{region_id}/filterByName")
    public ResponseEntity<?> getFilterSubTransactionGroup(
            @PathVariable("region_id") String region,
            @RequestParam("stGroup") String stGroup,
            Pageable pageable) {
        Page<SubTransactionGroupDTO> response =
                subTransactionService.filterSubTransactionGroupsByRegionAndName(region, stGroup, pageable);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/sub-transaction-group")
    private ResponseEntity<?> saveSubTransactionGroup(@RequestBody SubTransactionGroupReq stgReq) {
        SubTransactionGroup savedGroup = subTransactionService.saveSubTransactionGroup(stgReq);
        return ResponseEntity.ok("Saved Successfully");
    }
    @PutMapping("/sub-transaction-group/{id}")
    private ResponseEntity<?> editSubTransactionGroup(@PathVariable("id") String id,
                                                      @RequestBody SubTransactionGroupReq editReq) {
        SubTransactionGroup updated = subTransactionService.editSubTransactionGroup(id, editReq);
        return ResponseEntity.ok("Update SubTransaction Successfully");
    }
    @PutMapping("/assign-sub-service/{id}")
    private ResponseEntity<?> assignSubTransactionGroupToServices(@PathVariable("id") String groupId,
                                                                  @RequestBody ReqBody reqBody) {
        subTransactionService.assignSubTransactionGroupToServices(groupId, reqBody.getServices());
        return ResponseEntity.ok("Update SubTransaction Successfully");
    }
}
