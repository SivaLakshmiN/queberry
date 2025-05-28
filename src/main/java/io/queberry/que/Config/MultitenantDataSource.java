//package io.queberry.que.Config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Profile;
//import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//

//@Slf4j
////@Configuration
//@Profile("dev")
//@Component
//public class MultitenantDataSource extends AbstractRoutingDataSource {
//
//    public Map<Object, Object> resolvedDataSources = new HashMap<>();
//
//    private Object defaultTargetDataSource;
//
//    private List<Tenants> configList;
//
//    @Autowired
//    private RedisSequenceEngine sequenceEngine;
//
//    @Autowired
//    public TenantsRepository tenantsRepository;
//    private boolean lenientFallback = false;
//
//    private DatabaseConfig databaseConfig;
//
//    @Override
//    protected Object determineCurrentLookupKey() {
////        log.info("in determine current lookup {}", TenantContext.getCurrentTenant());
////        if(Objects.isNull(TenantContext.getCurrentTenant())){
////            log.info("in if");
////        }else{
////            log.info("in else");
////        }
//        return TenantContext.getCurrentTenant();
//    }
//
//    @Override
//    public void afterPropertiesSet() {
//        log.info("in afterPropertiesSet");
//        configList = tenantsRepository.findAll();
////        log.info("config list {} ", configList);
//        for (Tenants config : configList) {
//            buildDataSource(config);
//        }
//
//        sequenceEngine.sequenceManager(configList);
//    }
//
////    add new tenant to the datasource list
//    public boolean addToDSList(String tenantId){
//        Optional<Tenants> t = tenantsRepository.findByTenantId(tenantId);
////        log.info("data{}", t.get());
//        buildDataSource(t.get());
//        return true;
////        sequenceEngine.loadSequenceManager(t.get());
//    }
//
////    common datasource builder
//    public void buildDataSource(Tenants config){
//        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//        try {
//            String tenantId = config.getTenantId();
//
//            Object lookupKey = resolveSpecifiedLookupKey(tenantId);
//
//            dataSourceBuilder.driverClassName(config.getDbDriver());
//            dataSourceBuilder.username(config.getDbUserName());
//            dataSourceBuilder.password(config.getDbPassword());
////            dataSourceBuilder.url(config.getDbConnectionURL().replaceAll("mysql:3306","192.168.131.176:3306").replaceAll("192.168.131.180:3306","192.168.131.176:3306"));
//            dataSourceBuilder.url(config.getDbConnectionURL());
//
//
////                DataSource dataSource = resolveSpecifiedDataSource(dataSourceBuilder.build());
//            DataSource dataSource = (DataSource) dataSourceBuilder.build();
//            dataSource.setTestWhileIdle(true);
////            log.info("lookup key {}", lookupKey);
//            resolvedDataSources.put(lookupKey, dataSource);
////            log.info("resolved ds {}",resolvedDataSources);
//        } catch (Exception exp) {
//            throw new RuntimeException("Problem in tenant datasource:" + exp);
//        }
//    }
//
//    @NotNull
//    protected DataSource determineTargetDataSource() {
////        log.info("in determine target datasource");
////        Assert.notNull(this.resolvedDataSources, "DataSource router not initialized");
//        Object lookupKey = determineCurrentLookupKey();
//        DataSource dataSource = (DataSource) this.resolvedDataSources.get(lookupKey);
//
////        log.info("datasource {}", dataSource);
////        if (dataSource == null && (this.lenientFallback || lookupKey == null)) {
////            dataSource = this.resolvedDefaultDataSource;
////        }
//        if (dataSource == null) {
//            throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
//        }
//        return dataSource;
//    }
//
//    public MultitenantDataSource() {
//        log.info("in multi tenant datasource");
////        String url = "jdbc:mysql://qbapp-db.mysql.database.azure.com:3306/queberry?createDatabaseIfNotExist=true&serverTimezone=UTC&useSSL=true&requireSSL=false&autoReconnect=true";
//        String url= "jdbc:mysql://localhost:3306/queberry?createDatabaseIfNotExist=true&useUnicode=yes&characterEncoding=UTF-8&useSSL=false&autoReconnect=true";
//
//        DataSourceBuilder factory = DataSourceBuilder
//                .create().driverClassName("com.mysql.cj.jdbc.Driver")
//                .username("root")
//                .password("root")
////                .username("qbdbadmin")
////                .password("P@ssw0rd@123")
//                .url(url);
//        DataSource ds = (DataSource) factory.build();
//        ds.setTestWhileIdle(true);
//        resolvedDataSources.put("queberry", ds);
////        afterPropertiesSet();
//    }
//
////    @Bean
////    public DataSource dataSource() {
////        log.info("in bean ds");
////
////        setTargetDataSources(resolvedDataSources);
////        return customDataSource;
////    }
//
//
//}
