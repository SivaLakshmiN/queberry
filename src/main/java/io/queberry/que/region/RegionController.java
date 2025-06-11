package io.queberry.que.region;

import io.queberry.que.branch.Branch;
import io.queberry.que.branch.BranchController;
import io.queberry.que.branch.BranchData;
import io.queberry.que.branch.BranchRepository;
import io.queberry.que.branchGroup.BranchGroup;
import io.queberry.que.branchGroup.BranchGroupRepository;
import io.queberry.que.config.JwtTokenUtil;
import io.queberry.que.employee.Employee;
import io.queberry.que.employee.EmployeeRepository;
import io.queberry.que.role.Role;
import io.queberry.que.role.RoleRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RegionController {


    private final RegionRepository regionRepository;

    private final RegionService regionService;

    private final BranchGroupRepository branchGroupRepository;

    private final BranchRepository branchRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository rolesRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping("/regions")
    public ResponseEntity<?> getAllRegions(HttpServletRequest request, Pageable pageable){
        String token = request.getHeader("Authorization").substring(7);
        if(jwtTokenUtil.isTokenExpired(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT token expired!!");
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        Employee employee = employeeRepository.findByUsername(username);

        Set<Role> roles = employee.getAuthorities();
        if(roles.contains(rolesRepository.findByName("PRODUCT_ADMIN")) || roles.contains(rolesRepository.findByName("ORG_ADMIN"))) {
            log.info("prodcut admin");
            return ResponseEntity.ok(regionRepository.findAll(pageable));
        }
        else {
            log.info("not admin");
            Set<String> regions = new HashSet<>();
            regions.add(employee.getRegion());
            Page<Region> regionsPage = BranchController.getPage(regions, pageable);
            log.info("count:{}",regionsPage.getSize());
            return ResponseEntity.ok(regionsPage);
        }
    }


    @GetMapping("/regions/filterByName")
    public ResponseEntity<?> getFilterRegions(HttpServletRequest request,@RequestParam("region") String region, Pageable pageable){
        Employee employee = employeeRepository.findByUsername(request.getUserPrincipal().getName());
        Set<Role> roles = employee.getAuthorities();
        if(roles.contains(rolesRepository.findByName("PRODUCT_ADMIN")) || roles.contains(rolesRepository.findByName("ORG_ADMIN"))) {
            log.info("product admin");
            return ResponseEntity.ok(regionRepository.findByNameContainingIgnoreCase(region,pageable));
        }
        else {
            log.info("not admin");
            Set<String> regions = new HashSet<>();

            String r = (employee.getRegion().toLowerCase().contains((region.toLowerCase())) ? employee.getRegion() : null);
            regions.add(r);

            Page<Region> regionsPage = BranchController.getPage(regions, pageable);
            log.info("count:{}",regionsPage.getSize());
            return ResponseEntity.ok(regionsPage);
        }
    }


    @PostMapping("/regions")
    public ResponseEntity<?> saveRegion(@RequestBody Region region){
        return ResponseEntity.ok(regionRepository.save(region));
    }

//    @PutMapping("/regions/{id}")
//    public ResponseEntity<?> editRegion(@PathVariable String id,@RequestBody Region region){
//       return regionService.editRegion(id, region);
//    }

    @PostMapping("/branchGroup/{id}")
    public ResponseEntity<?> saveBranchGroup(@RequestBody BranchGroup branchGroup, @PathVariable("id") Region region){
        BranchGroup branchGroup1 =  branchGroupRepository.save(branchGroup);
        log.info("branch group {}", branchGroup1);
        Set<String> branchGroups = region.getBranchGroup();
        branchGroups.add(branchGroup1.getName());
        log.info("region branch groups {}", branchGroups);
        region.setBranchGroup(branchGroups);
        regionRepository.save(region);
        return ResponseEntity.ok(branchGroup1);
    }

    @PutMapping("/branchGroup/{id}")
    public ResponseEntity<?> updateBranchGroup(@PathVariable("id") BranchGroup branchGroupMain, @RequestBody BranchGroup branchGroup){
        branchGroupMain.setName(branchGroup.getName());
        branchGroupMain.setBranches(branchGroup.getBranches());
        branchGroupMain.setActive(branchGroup.isActive());
        return ResponseEntity.ok(branchGroupRepository.save(branchGroupMain));
    }

    @GetMapping("/branchGroupByRegion/{id}")
    public ResponseEntity<?> getBranchGroupByRegion(@PathVariable("id") Region region){
        Set<String> bg =region.getBranchGroup();
        Set<BranchGroup> branchGroups = new HashSet<>();
        for (String b: bg) {
            BranchGroup s = branchGroupRepository.findById(b).orElse(null);
            branchGroups.add(s);
        }
        return ResponseEntity.ok(branchGroups);
    }

    @GetMapping("/branchesByBranchGroup/{id}")
    public ResponseEntity<?> getBranchesByBranchGroup(@PathVariable("id") BranchGroup branchGroup){
        ModelMapper modelMapper = new ModelMapper();
        Set<String> bg = branchGroup.getBranches();
        Set<BranchData> branches = new HashSet<>();
        for (String b: bg) {
            Branch branch = branchRepository.findByBranchKey(b);
            BranchData res = modelMapper.map(branch, BranchData.class);
            branches.add(res);
        }
        return ResponseEntity.ok(branches);
    }

}
