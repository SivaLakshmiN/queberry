package io.queberry.que.service;

import io.queberry.que.dto.BranchDTO;
import io.queberry.que.dto.BranchRequest;
import io.queberry.que.dto.Capacity;
import io.queberry.que.dto.ServiceGroupRequest;
import io.queberry.que.entity.Branch;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface BranchService {

    Set<BranchDTO> getActiveBranches();
    Set<BranchDTO> getActiveWebPrinterBranches();
    String getActiveBranchCount();
    List<BranchDTO> getAllBranches();
    BranchDTO getBranchById(String id);
    Branch createBranch(Branch branch);
    Branch updateBranch(String id, BranchRequest branch);
    boolean deleteBranch(String id);
    Page<Branch> getBranchesByRegionId(String regionId, Pageable pageable);
    Branch activateBranch(String branchId);
    Branch deActivateBranch(String branchId);
    Page<BranchDTO> filterBranchesByName(HttpServletRequest request, String region, String brName, Pageable pageable);
    Branch assignServiceGroup(String branchId, ServiceGroupRequest request);
    Capacity getBranchCapacity(String branchKey);
}

