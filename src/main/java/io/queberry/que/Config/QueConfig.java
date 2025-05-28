package io.queberry.que.Config;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
@Slf4j
@Configuration
@EnableAsync
@EnableScheduling
//@EnableZuulProxy
//@EnableLifecycleEvents
@RequiredArgsConstructor
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class QueConfig {


    //private final HomeService homeService;

    @PostConstruct
    public void setupQue(){
        log.info("Setting up Queberry");
    }

//    @Bean
//    public Home home(){
//        return homeService.getHome();
//    }

    /*@Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster threadPoolAndSecurityAwareEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster
                = new SimpleApplicationEventMulticaster();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        //Creates a work-stealing thread pool using all available processors as its target parallelism level.
        Executor delegatedExecutor = Executors.newWorkStealingPool();
        Executor delegatingExecutor =
                new DelegatingSecurityContextExecutor(delegatedExecutor, securityContext);
        eventMulticaster.setTaskExecutor(delegatingExecutor);
        return eventMulticaster;
    }*/

    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
