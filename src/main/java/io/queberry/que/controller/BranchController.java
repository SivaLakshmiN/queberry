package io.queberry.que.controller;

import io.queberry.que.dto.BranchDTO;
import io.queberry.que.dto.BranchRequest;
import io.queberry.que.dto.Capacity;
import io.queberry.que.dto.ServiceGroupRequest;
import io.queberry.que.entity.Branch;
import io.queberry.que.exception.DataNotFoundException;
import io.queberry.que.exception.QueException;
import io.queberry.que.service.BranchService;
import jakarta.servlet.http.HttpServletRequest;
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

@RestController
@RequestMapping("/api")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @GetMapping("/branches/active")
    public ResponseEntity<Set<BranchDTO>> getActiveBranches() {
        Set<BranchDTO> branchData = branchService.getActiveBranches();
        return ResponseEntity.ok(branchData);
    }

    @GetMapping("/branches/webPrinter/active")
    public ResponseEntity<Set<BranchDTO>> getActiveWBranches() {
        Set<BranchDTO> activeBranches = branchService.getActiveWebPrinterBranches();
        return ResponseEntity.ok(activeBranches);
    }

    @GetMapping("/branches/activeCount")
    public ResponseEntity<?> getActiveBranchCount() {
        String count = branchService.getActiveBranchCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/branches")
    public ResponseEntity<List<BranchDTO>> getAllBranches() {
        List<BranchDTO> branches = branchService.getAllBranches();
        return ResponseEntity.ok(branches);
    }
    @GetMapping("/branches/{id}")
    public ResponseEntity<BranchDTO> getBranchById(@PathVariable String id) {
        try {
            BranchDTO branch = branchService.getBranchById(id);
            return ResponseEntity.ok(branch);
        } catch (QueException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/branches/save")
    public ResponseEntity<?> createBranch(@RequestBody Branch branch) {
        Branch createdBranch = branchService.createBranch(branch);
        return ResponseEntity.ok("Branch Created Successfully");
    }

    @PutMapping("/branches/branch/{id}")
    public ResponseEntity<?> editBranch(@PathVariable String id, @RequestBody BranchRequest branchInfo) {
        try {
            Branch updatedBranch = branchService.updateBranch(id, branchInfo);
            return ResponseEntity.ok("Branch Updated Successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Branch not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating branch");
        }
    }


    @DeleteMapping("/branches/{id}")
    public ResponseEntity<Boolean> deleteBranch(@PathVariable String id) {
        boolean deleted = branchService.deleteBranch(id);
        return ResponseEntity.ok(deleted);
    }

    @GetMapping("/branches/region")
    public ResponseEntity<Page<Branch>> getBranchesByRegion(
            @RequestParam(value = "regionId") String region,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        int pageNumber = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Branch> branches = branchService.getBranchesByRegionId(region, pageable);  // <-- corrected method name

        return branches.hasContent()
                ? ResponseEntity.ok(branches)
                : ResponseEntity.noContent().build();
    }

    @PutMapping("/branch/{id}/activate")
    public ResponseEntity<?> activateBranch(@PathVariable("id") String branchId) {
        try {
            Branch activatedBranch = branchService.activateBranch(branchId);
            return ResponseEntity.ok("Updated Successfully");  // Return the activated branch
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while activating the branch");
        }
    }

    @PutMapping("/branch/{id}/deActivate")
    public ResponseEntity<?> deActivateBranch(@PathVariable("id") String branchId) {
        try {
            Branch deactivatedBranch = branchService.deActivateBranch(branchId);
            return ResponseEntity.ok("Updated Successfully");
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deactivating the branch");
        }
    }
    @GetMapping("/branches/{id}/filterByName")
    public ResponseEntity<?> getFilterByName(HttpServletRequest request,
                                             @PathVariable("id") String region,
                                             @RequestParam("branch") String brName,
                                             Pageable pageable) {
        Page<BranchDTO> filteredBranches = branchService.filterBranchesByName(request, region, brName, pageable);
        return ResponseEntity.ok(filteredBranches);
    }
    @PutMapping("/branches/{id}/assignServiceGroup")
    public ResponseEntity<?> assignServiceGroup(@PathVariable("id") String branchId,
                                                @RequestBody ServiceGroupRequest request) {
        try {
            Branch updatedBranch = branchService.assignServiceGroup(branchId, request);
            return ResponseEntity.ok(updatedBranch);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Failed to assign service group");
        }
    }
    @GetMapping("/branches/branchCapacity/{key}")
    public ResponseEntity<?> getCapacity(@PathVariable("key") String branchKey) {
        try {
            Capacity capacity = branchService.getBranchCapacity(branchKey);
            return ResponseEntity.ok(capacity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Branch capacity info not found");
        }
    }
}
