package io.queberry.que.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigurationEngine {

    //private final WebSocketOperations messageSendingOperations;

    private final ConfigurationService configurationService;

    //private final ConfigurationDataLoader configurationDataLoader;

//    @TransactionalEventListener(fallbackExecution = true)
//    public void onConfigChange(ConfigurationEvent event) throws InterruptedException {
//        Thread.sleep(2000);
//        ConfigurationResource configuration = configurationService.getGlobalConfiguration();
//        messageSendingOperations.send("/notifications/"+ TenantContext.getBranchKey() +"/config",configuration);
//        log.info("Configuration Changed Event : {} , Global Configuration : {}",event,configuration);
//    }

}
