package io.queberry.que.config;//package io.queberry.que.config;
//import io.queberry.que.entity.Branch;
//import io.queberry.que.entity.SequenceManager;
//import io.queberry.que.entity.Service;
//import io.queberry.que.entity.SharedSequenceManager;
//import io.queberry.que.repository.BranchRepository;
//import io.queberry.que.repository.ServiceRepository;
//import io.queberry.que.repository.TokenRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
//import org.springframework.context.annotation.Configuration;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPool;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.*;
//
//@Slf4j
//@Configuration
////@Profile("!dev")
//@RequiredArgsConstructor
//public class RedisSequenceEngine {
//    @Autowired
//    private BranchRepository branchRepository;
//    @Autowired
//    private TokenRepository tokenRepository;
//
//    @Autowired
//    private ServiceRepository serviceRepository;
//
//    //    @Value("${redis-ip}")
//    @Value("${spring.redis.host}")
//    private String redisIP;
//
//    //    private final JedisPool jedisPool = new JedisPool("10.11.74.99", 6379);
////    private final JedisPool jedisPool = new JedisPool("192.168.131.148", 6379);
//    private JedisPool jedisPool;
//    private static final String BRANCH_SEQ_PREFIX = "seq:";
//    private static final String BRANCH_SHARED_SEQ_PREFIX = "shared:seq:";
//
//    public Map<String,Map<String, SequenceManager>> branchSequenceManager = new HashMap<>(0);
//    public Map<String,Map<String, SharedSequenceManager>> branchSharedSequenceManager = new HashMap<>(0);
//
////    @Bean
////    @Profile("!dev")
////    public void sequenceMgr(){
////        log.info("redis ip {}", redisIP);
////        jedisPool = new JedisPool(redisIP, 6379);
////        TenantContext.setCurrentTenant("queberry");
////        Set<Branch> branchSet = branchRepository.findByActiveTrue();
////        setSequenceManagerByBranches(branchSet);
////        TenantContext.clear();
////    }
////
////    @Bean
////    @Profile("dev")
////    public int sequenceManager(List<Tenants> configList){
////        log.info("setting sequence manager");
////        jedisPool = new JedisPool(redisIP, 6379);
////        for (Tenants config : configList) {
////            log.info("tenant:{}", config.getTenantId());
////            if(!config.getTenantId().equals("queberry")){
////                log.info("set tenant");
////                TenantContext.setCurrentTenant(config.getTenantId());
////                Set<Branch> branchSet = branchRepository.findByActiveTrue();
////                setSequenceManagerByBranches(branchSet);
////                TenantContext.clear();
////            }
////        }
////        return 1;
////    }
//
////    @Bean
////    @Profile("dev")
////    public Map<String,Map<String,SequenceManager>> sequenceManager(List<Tenants> tenantList){
////        log.info("setting sequence manager");
////        for (Tenants config : tenantList) {
//////            updateTables(config.getTenantId());
////            branchSequenceManager = loadSequenceManager(config.getTenantId());
////        }
//////        log.info("sequnce manager size:" + sequenceManager.size());
////        return branchSequenceManager;
////    }
//
//
////    public Map<String,Map<String,SequenceManager>> loadSequenceManager(String tenantId){
////
//    ////        log.info("tenant config name:" + tenantId);
////        Map<String,Map<String,SequenceManager>> bsm = new HashMap<>(0);
////        if (!tenantId.equals("queberry")) {
////            TenantContext.setCurrentTenant(tenantId);
////            Set<Branch> branchSet = branchRepository.findByActiveTrue();
////             setSequenceManagerByBranch(branchSet);
////            TenantContext.clear();
////        }
////        return bsm;
////    }
//
//    public Map<String,Map<String,SequenceManager>> setSequenceManagerByBranch(Set<Branch> branches){
//        branches.forEach(branch -> {
////            log.info("branch name:" + branch.getId());
////            Map<String, SequenceManager> sequenceManager1 = setSequenceManagerByBranch(branch);
//            setSequenceManagerByBranchWithSharedseq(branch);
////            log.info("service sequence manager {}", sequenceManager1);
////            branchSequenceManager.put(branch.getBranchKey(), sequenceManager1);
//            log.info("branch sequence manager {}", branchSequenceManager.size());
//            log.info("branch manager1 length {}", branchSharedSequenceManager.size());
//        });
//        return branchSequenceManager;
//    }
//
//    public void setSequenceManagerByBranchWithSharedseq(Branch branch) {
//        Set<String> services = branch.getServices();
//        Map<String,SequenceManager> sequenceManager1 =  new HashMap<>(0);
////        log.info("sequence manager1 length {}", sequenceManager1.size());
//        Map<String,SharedSequenceManager> sequenceManagerMap =  new HashMap<>(0);
////        log.info("sequenceManagerMap length {}", sequenceManagerMap.size());
//        services.forEach(service -> {
////            log.info("service name:" + service.getId());
////            if(service.isActive()){
////                if(service.isSharedSeq()){
//////                    log.info("in shared seq {}", service.getName());
////                    setTokenForSharedSeq(service, branch.getBranchKey() ,sequenceManagerMap);
////                }else {
//////                    log.info("in seq {}", service.getName());
////                    setTokenNumber(service, branch.getBranchKey(),sequenceManager1);
////                }
////            }else{
////                sequenceManager1.remove(service.getId());
////                sequenceManagerMap.remove(service.getId());
////            }
//        });
////        log.info("branchSequenceManager length {}", sequenceManager1.size());
////        log.info("branchSharedSequenceManager length {}", sequenceManagerMap.size());
//        branchSequenceManager.put(branch.getBranchKey(), sequenceManager1);
//        branchSharedSequenceManager.put(branch.getBranchKey(),sequenceManagerMap);
////        return sequenceManager1;
//    }
//
//
//    //
//    public void setSequenceManagerByBranches(Set<Branch> branches){
//        branches.forEach(branch -> setSequenceManagerWithRedis(branch.getServices(), branch.getBranchKey(), false));
//    }
//
//    public void setSequenceManagerWithRedis(Set<String> services, String branchKey, boolean reset){
//        log.info("branch key:{}", branchKey);
//        try (Jedis jedis = jedisPool.getResource()) {
//            Set<String> services1 = new HashSet<>();
//            Map<String, Integer> map = new HashMap<>();
//            Service service = new Service();
//            Set<String> existingSharedSeq = new HashSet<>();
//
////            for (Set<Service> service : services) {
////
////                if (service.isActive()) {
////                    if (service.isSharedSeq()) {
//////                        log.info("{}", services1.size());
////                        map.put(service.getSharedSequence().getId(), services1.size());
////                        services1 = serviceRepository.findBySharedSequence(service.getSharedSequence());
////                        setTokenForSharedSeq(services1, branchKey, service.getSharedSequence(),reset);
////                    } else {
//////                        log.info("not shared {}", service.getId());
////                        setTokenNumber(service, branchKey, reset);
////                    }
////                } else {
//            jedis.del(BRANCH_SEQ_PREFIX + branchKey + ":" + service.getId());
//            jedis.del(BRANCH_SHARED_SEQ_PREFIX + branchKey + ":" + service.getSharedSequence().getId());
////                }
////            }
//        }catch   (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//
//    // shared sequence
////    private void setTokenForSharedSeq(Set<Service> service, String branchKey, SharedSequence sharedSeq ,boolean reset) {
////        try (Jedis jedis = jedisPool.getResource()) {
//////            Token lastToken = tokenRepository.findByCreatedAtBetweenAndBranchAndServiceOrderByCreatedAtDesc(LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX), branchKey ,service).stream().findFirst().orElse(null);
//////            Token lastToken = tokenRepository.findByBranchAndServiceOrderByCreatedAtDesc(branchKey ,service).stream().findFirst().orElse(null);
//////            Token lastToken = tokenRepository.findByCreatedAtBetweenAndBranchAndServiceInOrderByCreatedAtDesc(LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX), branchKey ,service).stream().findFirst().orElse(null);
////            Token lastToken = tokenRepository.findByCreatedAtBetweenAndBranchAndServiceInOrderByCreatedAtDesc(LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX), branchKey ,service).stream().findFirst().orElse(null);
////            String branchSharedSeqKey = BRANCH_SHARED_SEQ_PREFIX + branchKey + ":" + sharedSeq.getId();
////            if (lastToken != null && !reset) {
////                jedis.set(branchSharedSeqKey,  lastToken.getNumber()+1 + ":" +sharedSeq.getSequenceEnd() );
////            }else{
////                jedis.set(branchSharedSeqKey, sharedSeq.getSequenceStart() +":"+ sharedSeq.getSequenceEnd());
////            }
////
////        }catch (Exception e){
////            e.printStackTrace();
////        }
////    }
////
////    // set token number for each service
////    private void setTokenNumber(Service service, String branchKey, boolean reset) {
////        try (Jedis jedis = jedisPool.getResource()) {
//////            Token lastToken = tokenRepository.findByCreatedAtBetweenAndBranchAndServiceOrderByCreatedAtDesc(LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX), branchKey, service).stream().findFirst().orElse(null);
////            Token lastToken = tokenRepository.findByCreatedAtBetweenAndBranchAndServiceIdOrderByCreatedAtDesc(LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX), branchKey, service.getId()).stream().findFirst().orElse(null);
////            String branchSeqKey = BRANCH_SEQ_PREFIX + branchKey + ":" + service.getId();
//////            log.info(branchSeqKey);
////            if (lastToken != null && !reset) {
////                jedis.set(branchSeqKey, lastToken.getNumber()+1 + ":" + service.getSequenceEnd());
////            }else{
////                jedis.set(branchSeqKey, service.getSequenceStart() + ":" + service.getSequenceEnd());
////            }
////        }catch (Exception e){
////            e.printStackTrace();
////        }
////    }
////
////    public void removeBranchFromSequence(String branchKey){
////        String seqPattern = "seq:" + branchKey + ":*";
////        deleteKeys(seqPattern);
////        String sharedSeqPattern = "shared:seq:" + branchKey + ":*";
////        deleteKeys(sharedSeqPattern);
////    }
////
////    private void deleteKeys(String pattern){
////        String cursor = "0";
////        ScanParams scanParams = new ScanParams();
////        scanParams.match(pattern);
////        scanParams.count(100); // Number of keys to scan in each iteration
////
////        try(Jedis jedis = jedisPool.getResource()) {
////            // Scan and delete keys in batches
////            do {
////                ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
////                cursor = scanResult.getCursor();
////                // Delete found keys
////                for (String key : scanResult.getResult()) {
////                    jedis.del(key);
////                }
////            } while (!cursor.equals("0"));
////        }catch (Exception e){
////            e.printStackTrace();
////        }
////    }
////
////    public Integer getToken(String branchKey, Service service){
////        int number = 0;
////        try(Jedis jedis = jedisPool.getResource()) {
////            String key = BRANCH_SEQ_PREFIX + branchKey + ":" + service.getId();
////            String[] sequenceParts = jedis.get(key).split(":");
////            int currentNumber = Integer.parseInt(sequenceParts[0]);
////            int endNumber = Integer.parseInt(sequenceParts[1]);
//////            log.info(sequenceParts[0] + ":" + sequenceParts[1]);
////            if (currentNumber > endNumber) {
////                // Reset sequence if we've reached the end
////                number = resetSequence(jedis, key, service.getSequenceStart(), service.getSequenceEnd());
////            } else {
////                // Increment current number
////                number = incrementSequence(jedis, key, currentNumber, endNumber);
////            }
////        }catch (Exception e){
////            e.printStackTrace();
////        }
////        return number;
////    }
////
////    public Integer getSharedSeqToken(String branchKey, SharedSequence sharedSequence){
////        int number = 0;
////        try(Jedis jedis = jedisPool.getResource()) {
////            String key = BRANCH_SHARED_SEQ_PREFIX + branchKey + ":" + sharedSequence.getId();
////            String[] sequenceParts = jedis.get(key).split(":");
//////            log.info(sequenceParts[0] + "" + sequenceParts[1]);
////            int currentNumber = Integer.parseInt(sequenceParts[0]);
////            int endNumber = Integer.parseInt(sequenceParts[1]);
////
////            if (currentNumber > endNumber) {
////                // Reset sequence if we've reached the end
////                number = resetSequence(jedis, key, sharedSequence.getSequenceStart(), sharedSequence.getSequenceEnd());
////            } else {
////                // Increment current number
////                number = incrementSequence(jedis, key, currentNumber, endNumber);
////            }
////        }catch (Exception e){
////            e.printStackTrace();
////        }
////        return number;
////    }
////
////    private int resetSequence(Jedis jedis,String key, int sequenceStart, int sequenceEnd) {
////        int num = (sequenceStart +1);
////        jedis.set(key, num + ":" + sequenceEnd);
////        log.info("Reset sequence for key {}: {}", key, jedis.get(key));
////        return sequenceStart;
////    }
////
////    private int incrementSequence(Jedis jedis,String key, int currentNumber, int endNumber) {
////        int num = (currentNumber + 1);
////        jedis.set(key, num + ":" + endNumber);
////        return currentNumber;
////    }
//}
//
