package io.queberry.que.branch;

import io.queberry.que.serviceGroup.ServiceGroupDTO;
import io.queberry.que.exception.DataNotFoundException;
import io.queberry.que.exception.QueException;
import io.queberry.que.dto.Capacity;
import io.queberry.que.serviceGroup.ServiceGroupRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
@Slf4j
@RestController
@RequestMapping("/api")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @GetMapping("/branches/active")
    public ResponseEntity<Set<BranchDTO>> getActiveBranches() {
        log.info("Fetching all active branches");
        Set<BranchDTO> branchData = branchService.getActiveBranches();
        return ResponseEntity.ok(branchData);
    }

    @GetMapping("/branches/webPrinter/active")
    public ResponseEntity<Set<BranchDTO>> getActiveWBranches() {
        log.info("Fetching active web printer branches");
        Set<BranchDTO> activeBranches = branchService.getActiveWebPrinterBranches();
        return ResponseEntity.ok(activeBranches);
    }

    @GetMapping("/branches/activeCount")
    public ResponseEntity<?> getActiveBranchCount() {
        log.info("Getting count of active branches");
        String count = branchService.getActiveBranchCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/branches")
    public ResponseEntity<List<BranchDTO>> getAllBranches() {
        log.info("Retrieving all branches");
        List<BranchDTO> branches = branchService.getAllBranches();
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/branch/{id}")
    public ResponseEntity<BranchDTO> getBranchById(@PathVariable("id") String id) {
        log.info("Fetching branch by ID: {}", id);
        try {
            BranchDTO branch = branchService.getBranchById(id);
            return ResponseEntity.ok(branch);
        } catch (QueException e) {
            log.warn("Branch not found: {}", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Error retrieving branch by ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/branches/save")
    public ResponseEntity<?> createBranch(@RequestBody Branch branch) {
        log.info("Creating new branch: {}", branch.getName());
        Branch createdBranch = branchService.createBranch(branch);
        return ResponseEntity.ok("Branch Created Successfully");
    }

    @PutMapping("/branches/branch/{id}")
    public ResponseEntity<?> editBranch(@PathVariable String id, @RequestBody BranchRequest branchInfo) {
        log.info("Updating branch with ID: {}", id);
        try {
            Branch updatedBranch = branchService.updateBranch(id, branchInfo);
            return ResponseEntity.ok("Branch Updated Successfully");
        } catch (NoSuchElementException e) {
            log.warn("Branch not found: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Branch not found");
        } catch (Exception e) {
            log.error("Error updating branch with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating branch");
        }
    }

    @DeleteMapping("/branches/{id}")
    public ResponseEntity<Boolean> deleteBranch(@PathVariable String id) {
        log.info("Deleting branch with ID: {}", id);
        boolean deleted = branchService.deleteBranch(id);
        return ResponseEntity.ok(deleted);
    }

    @GetMapping("/branches/{id}")
    public ResponseEntity<Page<Branch>> getBranchesByRegion(
            @RequestParam(value = "id") String regionId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        log.info("Getting branches for region ID: {}, page: {}, size: {}", regionId, page, size);

        int pageNumber = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Branch> branches = branchService.getBranchesByRegion(regionId, pageable);

        return branches.hasContent()
                ? ResponseEntity.ok(branches)
                : ResponseEntity.noContent().build();
    }

    @PutMapping("/branch/{id}/activate")
    public ResponseEntity<?> activateBranch(@PathVariable("id") String branchId) {
        log.info("Activating branch ID: {}", branchId);
        try {
            Branch activatedBranch = branchService.activateBranch(branchId);
            return ResponseEntity.ok("Updated Successfully");
        } catch (DataNotFoundException e) {
            log.warn("Branch not found for activation: {}", branchId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error activating branch ID: {}", branchId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while activating the branch");
        }
    }

    @PutMapping("/branch/{id}/deActivate")
    public ResponseEntity<?> deActivateBranch(@PathVariable("id") String branchId) {
        log.info("Deactivating branch ID: {}", branchId);
        try {
            Branch deactivatedBranch = branchService.deActivateBranch(branchId);
            return ResponseEntity.ok("Updated Successfully");
        } catch (DataNotFoundException e) {
            log.warn("Branch not found for deactivation: {}", branchId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error deactivating branch ID: {}", branchId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deactivating the branch");
        }
    }

    @GetMapping("/branches/{id}/filterByName")
    public ResponseEntity<?> getFilterByName(HttpServletRequest request,
                                             @PathVariable("id") String region,
                                             @RequestParam("branch") String brName,
                                             Pageable pageable) {
        log.info("Filtering branches by name '{}' in region '{}'", brName, region);
        Page<BranchDTO> filteredBranches = branchService.filterBranchesByName(request, region, brName, pageable);
        return ResponseEntity.ok(filteredBranches);
    }

    @PutMapping("/branches/{id}/assignServiceGroup")
    public ResponseEntity<?> assignServiceGroup(@PathVariable("id") String branchId,
                                                @RequestBody ServiceGroupRequest request) {
        log.info("Assigning service group to branch ID: {}", branchId);
        try {
            Branch updatedBranch = branchService.assignServiceGroup(branchId, request);
            return ResponseEntity.ok(updatedBranch);
        } catch (Exception e) {
            log.error("Failed to assign service group to branch ID: {}", branchId, e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Failed to assign service group");
        }
    }

    @GetMapping("/branches/services/{key}")
    public ResponseEntity<Set<ServiceGroupDTO>> getAllServiceGroups(@PathVariable("key") String branchKey) {
        log.info("Fetching all service groups for branch key: {}", branchKey);
        Set<ServiceGroupDTO> serviceGroups = branchService.getServiceGroupsByBranchKey(branchKey);
        return ResponseEntity.ok(serviceGroups);
    }

    @GetMapping("/branches/branchCapacity/{key}")
    public ResponseEntity<?> getCapacity(@PathVariable("key") String branchKey) {
        log.info("Getting capacity info for branch key: {}", branchKey);
        try {
            Capacity capacity = branchService.getBranchCapacity(branchKey);
            return ResponseEntity.ok(capacity);
        } catch (Exception e) {
            log.error("Capacity info not found for branch key: {}", branchKey, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Branch capacity info not found");
        }
    }
}
