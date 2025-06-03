package io.queberry.que.config.Dispenser;
import io.queberry.que.Counter.CounterRepository;
import io.queberry.que.Employee.EmployeeRepository;
import io.queberry.que.Session.SessionRepository;
import io.queberry.que.config.Theme.ThemeConfiguration;
import io.queberry.que.config.Theme.ThemeConfigurationRepository;
import io.queberry.que.entity.Language;
import io.queberry.que.exception.QueueException;
import io.queberry.que.service.ServiceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
public class DispenserConfigurationController {

    private final DispenserConfigurationRepository dispenserConfigurationRepository;
    private final ThemeConfigurationRepository themeConfigurationRepository;
    private final ServiceRepository serviceRepository;
    private final CounterRepository counterRepository;
    private final EmployeeRepository employeeRepository;
    //private final EmployeeSessionRepository employeeSessionRepository;
   //private final AssistanceRepository assistanceRepository;
    private final SessionRepository sessionRepository;
    //private final TokenRepository tokenRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public DispenserConfigurationController(DispenserConfigurationRepository dispenserConfigurationRepository, ThemeConfigurationRepository themeConfigurationRepository, ServiceRepository serviceRepository, CounterRepository counterRepository, EmployeeRepository employeeRepository, SessionRepository sessionRepository) {
        this.dispenserConfigurationRepository = dispenserConfigurationRepository;
        this.themeConfigurationRepository = themeConfigurationRepository;
        this.serviceRepository = serviceRepository;
        this.counterRepository = counterRepository;
        this.employeeRepository = employeeRepository;
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("/config/dispenser")
    public ResponseEntity<?> getDispenserConfig(HttpServletRequest request){
        log.info("request header in dispenser config {}",request.getHeader("X-TenantID"));
        Optional<DispenserConfiguration> dispenserConfiguration = dispenserConfigurationRepository.findByBranchKey(request.getHeader("X-TenantID"));
        if (dispenserConfiguration.isPresent()){
            return ResponseEntity.ok(dispenserConfiguration.get());
        }
        throw new QueueException("No Unique Dispenser Configuration Found",HttpStatus.PRECONDITION_FAILED);
    }

    @PutMapping("/config/dispenser")
    public ResponseEntity<?> editDispenserConfig(@RequestBody DispenserConfigurationResource resource){
        DispenserConfiguration dispenserConfiguration = dispenserConfigurationRepository.findByBranchKey(resource.getBranchKey()).orElse(null);
        if (dispenserConfiguration == null){
            dispenserConfiguration = new DispenserConfiguration(resource.getWelcomeMessage(), resource.getNumberOfTokens(), resource.isShowClock(), resource.getMessageAfterBusinessHours(),resource.isBlurBackground(),
            resource.isEnableServiceGroup(),resource.isShowBarcode(),resource.getBranchKey(), resource.isShowArabic(),resource.isShowWaitingCustomers(),resource.isShowWaitingTime(), resource.isShowInQueueAlert(), resource.isEidScan());
            dispenserConfiguration.setLanguageList(resource.getLanguages());
        }else{
           dispenserConfiguration.setWelcomeMessage(resource.getWelcomeMessage());
           dispenserConfiguration.setNumberOfTokens(resource.getNumberOfTokens());
           dispenserConfiguration.setShowClock(resource.isShowClock());
           dispenserConfiguration.setMessageAfterBusinessHours(resource.getMessageAfterBusinessHours());
           dispenserConfiguration.setBlurBackground(resource.isBlurBackground());
           dispenserConfiguration.setEnableServiceGroup(resource.isEnableServiceGroup());
           dispenserConfiguration.setShowBarcode(resource.isShowBarcode());
           dispenserConfiguration.setShowArabic(resource.isShowArabic());
           dispenserConfiguration.setShowWaitingCustomers(resource.isShowWaitingCustomers());
           dispenserConfiguration.setShowWaitingTime(resource.isShowWaitingTime());
           dispenserConfiguration.setShowInQueueAlert(resource.isShowInQueueAlert());
           dispenserConfiguration.setEidScan(resource.isEidScan());
           dispenserConfiguration.setLanguageList(resource.getLanguages());
        }
        dispenserConfiguration = dispenserConfigurationRepository.save(dispenserConfiguration);

        ThemeConfiguration themeConfiguration = themeConfigurationRepository.findByBranchKey(resource.getBranchKey()).orElse(null);
        if(themeConfiguration != null){
            themeConfiguration.setShowTime(resource.isShowClock());
            themeConfiguration.setShowArabic(resource.isShowArabic());
            themeConfiguration.setShowWaitingCustomers(resource.isShowWaitingCustomers());
            themeConfiguration.setShowWaitingTime(resource.isShowWaitingTime());
            themeConfiguration.setShowInQueueAlert(resource.isShowInQueueAlert());
            themeConfigurationRepository.save(themeConfiguration);
        }
        return ResponseEntity.ok(dispenserConfiguration);
    }

    @PutMapping("/config/dispenserConfig")
    public ResponseEntity<?> getBranchAudioConfig(@RequestBody DispenserConfigurationResource resource){
        return ResponseEntity.ok(dispenserConfigurationRepository.findByBranchKey(resource.getBranchKey()));
    }

    @Transactional
    @GetMapping("/config/dispenser/migrate")
    public ResponseEntity<?> delete(){
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        sessionRepository.deleteAllInBatch();
        log.info("sessions deleted");

        //assistanceRepository.deleteAllInBatch();
        log.info("assistance deleted");
        //tokenRepository.deleteAllInBatch();
        log.info("tokens deleted");
        counterRepository.deleteAllInBatch();
        log.info("counter deleted");
        //employeeSessionRepository.deleteAllInBatch();
        log.info("emp sessions deleted");
        employeeRepository.deleteAllInBatch();
        log.info("employee deleted");
        serviceRepository.deleteAll();
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();

        return ResponseEntity.ok("ok");
    }

//    @Transactional
//    public void truncateTables() {
//        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
//        entityManager.createNativeQuery("TRUNCATE TABLE que_assistance_sessions").executeUpdate();
//        entityManager.createNativeQuery("TRUNCATE TABLE que_session").executeUpdate();
//        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
//    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DispenserConfigurationResource{
        private String welcomeMessage;
        private Integer numberOfTokens;
        private boolean showClock;
        private String messageAfterBusinessHours;
        private boolean blurBackground;
        private boolean enableServiceGroup;
        private boolean showBarcode;
        private String branchKey;
        private boolean showArabic;
        private boolean showWaitingCustomers;
        private boolean showWaitingTime;
        private boolean showInQueueAlert;
        private boolean eidScan;
        private List<Language> languages;
    }

}
