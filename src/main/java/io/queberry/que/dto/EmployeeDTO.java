package io.queberry.que.dto;//package io.queberry.que.dto;
//
//import io.queberry.que.entity.*;
//import lombok.*;
//import lombok.extern.slf4j.Slf4j;
//
//import java.time.LocalDateTime;
//import java.util.Set;
//import java.util.TreeSet;
//import java.util.stream.Collectors;
//
//@Setter
//@Getter
//@ToString
//@NoArgsConstructor(force = true)
//@EqualsAndHashCode
//@Slf4j
//public class EmployeeDTO {
//    private String id;
//    private String username;
//    //    private String password;
//    private Set<Role> roles;
//    private String firstname;
//    private String middlename;
//    private String lastname;
//    private String counter;
//    private boolean active;
//    private boolean walkIn;
//    private boolean callByNumber;
//    private boolean enableAutoCall;
//    private boolean forceAutoCall;
//    private boolean park;
//    private boolean transferService;
//    private boolean transferCounter;
//    private boolean transferUser;
//    private boolean break_btn;
//    private boolean callAll; // both
//    private boolean callNew;
//    private boolean callTransfer;
//    private boolean showServiceList;
//    private Set<String> services;
//    private Set<String> second = new TreeSet<>();
//    private Set<String> third = new TreeSet<>();
//    private Set<String> fourth = new TreeSet<>();
//    private String tenant;
//    private Set<String> branches;
//    private String loggedCounter;
//    private String loggedBranch;
//    private LocalDateTime loggedTime;
//    private String region;
//    private boolean locked;
//
//    public EmployeeDTO(Employee employee) {
//        this.id = employee.getId();
//        this.username = employee.getUsername();
////        this.password = employee.getPassword();
//        this.firstname = employee.getFirstname();
//        this.middlename = employee.getMiddlename();
//        this.lastname = employee.getLastname();
//        this.tenant = employee.getTenant();
//        this.roles = employee.getRoles();
//        this.counter = employee.getCounter();
//        this.active = employee.isActive();
//        this.walkIn = employee.isWalkIn();
//        this.callByNumber = employee.isCallByNumber();
//        this.enableAutoCall = employee.isEnableAutoCall();
//        this.forceAutoCall = employee.isForceAutoCall();
//        this.park = employee.isPark();
//        this.transferService = employee.isTransferService();
//        this.transferCounter = employee.isTransferCounter();
//        this.transferUser = employee.isTransferUser();
//        this.break_btn = employee.isBreak_btn();
//        this.callAll = employee.isCallAll(); // both
//        this.callNew = employee.isCallNew();
//        this.callTransfer = employee.isCallTransfer();
//        this.showServiceList = employee.isShowServiceList();
//        this.services = employee.getServices();
//        this.second = employee.getSecond();
//        this.third = employee.getThird();
//        this.fourth = employee.getFourth();
////        this.branches = getBranchDTO(employee.getBranches());
//        this.tenant = employee.getTenant();
//        this.loggedCounter = employee.getLoggedCounter();
////        this.loggedBranch = employee.getLoggedBranch();
//        this.loggedTime = employee.getLoggedTime();
//        this.region = employee.getRegion();
//        this.locked = employee.isLocked();
//    }
//
//    private Set<ServiceDTO> getServiceDTO(Set<Service> services) {
//        return services.stream().map(ServiceDTO::new).collect(Collectors.toSet());
//    }
//
////    private Set<BranchEmployeeDTO> getBranchDTO(Set<Branch> branches){
////        return branches.stream().map(BranchEmployeeDTO::new).collect(Collectors.toSet());
//}
