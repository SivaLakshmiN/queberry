package io.queberry.que.config.Dispenser;

import io.queberry.que.assistance.Assistance;
import io.queberry.que.assistance.AssistanceRepository;
import io.queberry.que.branch.BranchRepository;
import io.queberry.que.config.Appointment.AppointmentService;
import io.queberry.que.config.ConfigurationService;
import io.queberry.que.config.Kpi.KpiService;
import io.queberry.que.config.Whatsapp.WhatsappConfiguration;
import io.queberry.que.config.Whatsapp.WhatsappConfigurationRepository;
import io.queberry.que.device.Device;
import io.queberry.que.device.DeviceRepository;
import io.queberry.que.enums.Gender;
import io.queberry.que.enums.Status;
import io.queberry.que.enums.Type;
import io.queberry.que.exception.QueueException;
import io.queberry.que.queue.QueueToken;
import io.queberry.que.queue.QueueTokenRepository;
import io.queberry.que.service.Service;
import io.queberry.que.service.ServiceRepository;
import io.queberry.que.ticker.TickerRepository;
import io.queberry.que.token.Token;
import io.queberry.que.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
//@RestController
@RestController
@RequestMapping("/api/dispenser")
@RequiredArgsConstructor
public class DispenserController {

    private final ServiceRepository serviceRepository;

    private final TokenEngine tokenEngine;

    private final TickerRepository tickerRepository;

    private final ConfigurationService configurationService;

    private final DeviceRepository deviceRepository;

    private final HttpServletRequest request;

    private final KpiService kpiService;

    private final AssistanceRepository assistanceRepository;

    private final QueueTokenRepository queueTokenRepository;

    private final BranchRepository branchRepository;
    private final WhatsappConfigurationRepository whatsappConfigurationRepository;
    private final TokenRepository tokenRepository;
//    private final ReportingService reportingService;

    //    @Autowired(required = false)
    private final AppointmentService appointmentService;

    private final DataService dataService;
    @PostMapping("/deleteData")
    public String deleteData() {
        try {
            dataService.deleteAllData();
            return "All records deleted successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/deleteAssistanceReport")
    public String deleteAssistanceReport() {
        try {
            dataService.deleteAssistanceReport();
            return "All records deleted successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }


    @PostMapping("/updateEmployeesData")
    public String updateEmployeesData() {
        try {
            dataService.updateEmpData();
            return "All records deleted successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/createTIndexes")
    public String createTIndexData() {
        try {
            dataService.createTIndex();
            return "Token Index created successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/createQCIndexes")
    public String createQCIndex() {
        try {
            dataService.createQCIndex();
            return "Qued counter Index created successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    @PostMapping("/createQTIndexes")
    public String createQTIndex() {
        try {
            dataService.createQTIndex();
            return "Queued token Index created successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    @PostMapping("/createCTIndexes")
    public String createCTIndex() {
        try {
            dataService.createCTIndex();
            return "Counter token Index created successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    @PostMapping("/createSerIndexes")
    public String createSerIndex() {
        try {
            dataService.createSerIndex();
            return "Service unique Index dropped successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/services")
    public ResponseEntity getactiveServices(){
        return ResponseEntity.ok(serviceRepository.findByActiveTrue(Sort.by(Sort.Order.asc("name"))));
    }

//    @GetMapping("/serviceGroups")
//    public ResponseEntity getAllServiceGroups() {
//        return ResponseEntity.ok(getDevice().getServiceGroups());
//    }

    @GetMapping("/config")
    public ResponseEntity config(){
        return ResponseEntity.ok(configurationService.getGlobalConfiguration());
    }

    @PostMapping("/token")
    public ResponseEntity<Token> issue(@RequestBody DispenserController.TokenRequest tokenRequest, HttpServletRequest request) throws Exception {
        String branchKey ;
        if(tokenRequest.getBranch().length()>0){
            log.info("in if");
            branchKey = tokenRequest.getBranch();
        }else{
            log.info("in else");
            branchKey = request.getHeader("X-TenantID");
        }

//        String branchKey = tenantID.substring(10);
        Token token = null;

        Service service = serviceRepository.findById(tokenRequest.getService()).orElse(null);

//        if (service == null){
//            service = serviceRepository.findBySubServices_Id(tokenRequest.getService());
//            SubService subService = subServiceRepository.findById(tokenRequest.getService()).orElse(null);
//            token = tokenEngine.create(service,subService,tokenRequest.getLanguage(), tokenRequest.getMobile(), tokenRequest.getMedium(), branchKey);
//        }
//        else {
        token = tokenEngine.create(service, null, tokenRequest.getLanguage(), tokenRequest.getMobile(), tokenRequest.getMedium(),branchKey, tokenRequest.getPriority(), tokenRequest.getPrivateCode());
//            log.info("token creted sucesfully");
//        }

        if (token.getType() == Type.DISPENSER) {
//            log.info("dispenser token");
            Assistance assistance = new Assistance(token, tokenRequest);

            QueueToken queueToken = QueueToken.of(token);
            log.info("{}",queueToken);
            log.info("{}",queueTokenRepository.findAll().size());
            queueToken = queueTokenRepository.save(queueToken);
            log.info("after {}",queueTokenRepository.findAll().size());
            log.info("{} added to Queue for the service . Queue Token : {}",token,queueToken);
            log.info("queued token created sucessfully");

//            try {
//                RedissonCacheOperations.putInCache(assistance.getId(), assistance, "AssistanceCache", assistance.getTokenRef());
//            }catch (Exception e){
//                log.info(e.getMessage());
//            }

            Assistance assistance1 = assistanceRepository.save(assistance);
            log.info("{} created for {}", assistance1, token);
        }

        if (token.getType() == Type.APPOINTMENT) {
            Assistance assistance = new Assistance(token);
            assistance = assistanceRepository.save(assistance);
            log.info("{} created for {} for Appointment {}", assistance, token, token.getAppointment());
        }

        if (token.getType() == Type.MEETING) {
            Assistance assistance = new Assistance(token);
            assistance = assistanceRepository.save(assistance);
            log.info("{} created for {} for Meeting", assistance, token);
        }

        return ResponseEntity.ok(token);
    }

    @PutMapping("/appointment/checkin")
    public ResponseEntity checkin(@RequestBody CheckinRequest checkinRequest){
        return ResponseEntity.ok(appointmentService.checkin(checkinRequest.getMobile(), checkinRequest.getCheckinCode()));
    }

    @PutMapping("/appointment/validateCheckin")
    public ResponseEntity validateCheckin(@RequestBody CheckinRequest checkinRequest){
        return ResponseEntity.ok(appointmentService.checkin(checkinRequest.getCheckinCode()));
    }

    @PutMapping("/appointment/mobile_checkin")
    public ResponseEntity mcheckin(@RequestBody CheckinRequest checkinRequest){
        return ResponseEntity.ok(appointmentService.checkin(checkinRequest.getMobile(), checkinRequest.getCheckinCode(),checkinRequest.getMedium()));
    }

    @GetMapping("/ticker")
    public ResponseEntity getTicker(){
        return ResponseEntity.ok(tickerRepository.findAll());
    }


    @PutMapping("/sendMail")
    public ResponseEntity sendMails(@RequestBody DispenserController.EmployeeMail employeeMail){
        log.info("in controller email:" + employeeMail.getEmpEMail());
        log.info("in controller msg:" + employeeMail.getMessage());
        kpiService.check(employeeMail.getEmpEMail(),employeeMail.getMessage());
        //employee.counter(null);
        //employee = employeeRepository.save(employee);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getTenant")
    public ResponseEntity getTenant(@RequestBody String mobile){
        log.info("get tenant for whatsapp");
        Optional<WhatsappConfiguration> whatsappConfiguration = whatsappConfigurationRepository.findByMobile(mobile);
        return whatsappConfiguration.<ResponseEntity>map(configuration -> ResponseEntity.ok(configuration.getTenant())).orElseGet(() -> ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Mobile number doesn't exist"));
    }

    @PutMapping("/liveServiceData")
    public ResponseEntity liveServiceData(@RequestBody TokenRequest tokenRequest){
        log.info("get service data after token");
        LiveServiceResponse liveServiceResponse = new LiveServiceResponse();
        LocalDateTime start =  LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end =  LocalDateTime.of(LocalDate.now(),LocalTime.MAX);
        Set<Assistance> assistances = assistanceRepository.findByCreatedAtBetweenAndSessionsServiceIdAndOptionalBranch(start,end,tokenRequest.getService(), Collections.singletonList(tokenRequest.getBranch()));
        assistances.addAll(assistanceRepository.findByCreatedAtBetweenAndServiceIdAndOptionalBranch(start,end,tokenRequest.getService(),Collections.singletonList(tokenRequest.getBranch())));
        log.info("asssistance size:{}", assistances.size());
        liveServiceResponse.setNoOfWaiting(assistances.stream().filter(assistance -> assistance.getStatus() == Status.SCHEDULED).collect(Collectors.toSet()).size());
        if(!assistances.isEmpty()) {
            log.info("assitance not empty");
            Long total = assistances.stream().filter(assistance -> assistance.getStatus() != Status.EXPIRED).map(Assistance::getWaitingTime)
                    .reduce(Long::sum).orElse(0L);
            liveServiceResponse.setAvgWaitTime(Math.ceil((double) total / assistances.size()));
            log.info("wait time set");
        }
        liveServiceResponse.setService(tokenRequest.getService());
        log.info("returning live service");
        return ResponseEntity.ok().body(liveServiceResponse);
    }

    public Device getDevice(){
        String deviceId = request.getHeader("device-id");
        Device device = deviceRepository.findByDeviceId(deviceId).orElse(null);
        if (device == null)
            throw new QueueException("Device not found - "+request.getHeader("device-id"), HttpStatus.PRECONDITION_FAILED);
        device.validate();
        return device;

    }

//    @GetMapping("/branch/services/active")
//    public ResponseEntity<?> getAllActiveServicesByBranch(HttpServletRequest request){
//        log.info("request header in getActiveServicesByBranch {}",request.getHeader("X-TenantID"));
//        Branch b = branchRepository.findByBranchKey(request.getHeader("X-TenantID"));
//        log.info("branch services");
//        log.info("{}", b.getServices());
//        Set<Service> services = b.getServices().stream().filter(Service::isActive)
//                .sorted(Comparator.comparing(Service::getName))
//                .collect(Collectors.toCollection(LinkedHashSet::new));
//
//        return ResponseEntity.ok(services);
//    }


    @GetMapping("/branch")
    public ResponseEntity<?> getBranchByBranchKey(HttpServletRequest request){
        log.info("request header in getBranch {}",request.getHeader("X-TenantID"));
        return ResponseEntity.ok(branchRepository.findByBranchKey(request.getHeader("X-TenantID")));
    }

//    @GetMapping("/tokenInfo/{id}")
//    public ResponseEntity<?> getTokenInfo(@PathVariable("id") String tokenId){
//        Token token = tokenRepository.findTokenById(tokenId).orElse(null);
//        TokenResponse tokenResponse = new TokenResponse();
//        if(token != null){
//            Integer count = queueTokenRepository.countByServiceIdAndCreatedAtLessThan(token.getService().getId(), token.getCreatedAt());
//            log.info("{}", count);
//            Report report = reportingService.getServiceReportByBranch(token.getService(),LocalDate.now(),LocalDate.now(),token.getBranch());
//            log.info("{}", report.getAvgWaitTime());
//            tokenResponse.setAvgWaitTime(report.getAvgWaitTime());
//            tokenResponse.setPeopleWaiting(count);
//            tokenResponse.setTokenInfo(token);
//        }
//        return ResponseEntity.ok(tokenResponse);
//    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TokenRequest {
        String branch = "";
        String service;
        String language;
        String mobile;
        Token.Medium medium;
        String cardNo;
        String dob;
        Boolean eidScanned;
        String emiratesId;
        String name;
        String eidphone;
        String nationality;
        String occupation;
        Gender gender;
        String email;
        Integer age;
        //        private boolean hasAppointment;
        Set<String> bookinIds;
        Integer priority = 1;
        String privateCode;
        private String accountId;
        private String accountName;
        private String salesforceId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CheckinRequest {
        String checkinCode;
        String mobile;
        Token.Medium medium;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TokenResponse {
        Token tokenInfo;
        Integer peopleWaiting;
        Double avgWaitTime;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmployeeMail{
        private String empEMail;
        private String message;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LiveServiceResponse{
        private String service;
        private Double avgWaitTime;
        private Integer noOfWaiting;
    }
}
