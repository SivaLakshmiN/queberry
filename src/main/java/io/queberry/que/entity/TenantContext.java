package io.queberry.que.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;

import java.util.Objects;

@Slf4j
@Profile("dev")
public class TenantContext {
    private static final ThreadLocal<String> currentTenant = new InheritableThreadLocal<>();

    public static String defaultTenant = "queberry";

    public static String defaultBranchKey;
//    public static DataSourceConfiguration ds;

    public static String getCurrentTenant() {
//        log.info("in tenant context {}", currentTenant.get());
        if(Objects.isNull(currentTenant.get())){
            setCurrentTenant(defaultTenant);
        }
        return currentTenant.get();
    }

    public static void setCurrentTenant(String tenant) {
//        log.info("in set current tenant {}", tenant);
        currentTenant.set(tenant);
    }

    public static void clear() {
        currentTenant.set(null);
    }

    public static String getBranchKey(){
        return defaultBranchKey;
    }

    public static void setBranchKey(String branchKey){
        defaultBranchKey = branchKey;
    }

}

