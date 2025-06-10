package io.queberry.que.config.Dispenser;

import io.queberry.que.config.Queue.QueueConfiguration;
import io.queberry.que.config.Redis.RedisSequenceEngine;
import io.queberry.que.config.Websocket.WebSocketOperations;
import io.queberry.que.counter.CounterDTO;
import io.queberry.que.counter.CounterStatusService;
import io.queberry.que.employee.EmployeeDTO;
import io.queberry.que.enums.TokenStatus;
import io.queberry.que.enums.Type;
import io.queberry.que.exception.QueueException;
import io.queberry.que.queue.*;
import io.queberry.que.resources.CounterAgentRequest;
import io.queberry.que.service.ServiceDTO;
import io.queberry.que.sharedSequence.SequenceManager;
import io.queberry.que.subService.SubService;
import io.queberry.que.token.Token;
import io.queberry.que.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.*;
import java.util.stream.Collectors;

import static io.queberry.que.config.Queue.QueueConfiguration.ServicePriority.USER;
import static io.queberry.que.counter.Counter.Type.COUNTER;

/**
 * TokenEngine handles the Token and Queue throughout it's Lifecycle
 * @author : Fahad Fazil
 * @since : 30/01/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class  TokenEngine {

    private final TokenRepository tokenRepository;

    private final QueueTokenRepository queueTokenRepository;

    private final Map<String, SequenceManager> sequenceManager;

    //    private final SequenceEngine sequenceEngine;
    private final RedisSequenceEngine sequenceEngine;

    private final CounterTokenRepository counterTokenRepository;

    private final AppointmentTokenRepository appointmentTokenRepository;

    private final QueueCounterRepository queueCounterRepository;
    private final WebSocketOperations messagingTemplate;
    private final CounterStatusService counterStatusService;

    /**
     * Method to create a Token for the passed service and add it to the Queue of the service
     *  -Find the Queue from the Service
     *  -Initialize a Token with the next number from the Queue . Counter will be assigned later when called by a Counter
     *  -Save the Token
     *  -Add the new Token to the Queue
     *  -Save the Queue
     */
//    public Token create(io.queberry.que.service.Service service, SubService subService, String language, String mobile, Token.Medium medium, String branchKey){
//
//        log.info("in token created");
//
//        Integer nextSequence;
//        //get the next sequence number and save the sequence manager
//        if(service.isSharedSeq()){
////            Map<String, SharedSequenceManager> sharedSequenceManagerMap = sequenceEngine.getBranchSharedSequenceManager().get(branchKey);
//            nextSequence = sharedSequenceManagerMap.get(service.getSharedSequence().getId()).getNext();
//        }else{
////            Map<String, SequenceManager> sequenceManagerMap = sequenceEngine.getBranchSequenceManager().get(branchKey);
//            nextSequence = sequenceManagerMap.get(service.getId()).getNext();
//        }
//
//        //Create a Token with the Service and  next number from the Queue . Counter will be assigned once the token is called
//        Token token = null;
//
//        token = new Token(service,subService,nextSequence,Type.DISPENSER,language,mobile,medium,branchKey);
//
//        //Save the token
//        token = tokenRepository.save(token);
//
//        //Log the created Token
//        log.info("Token created : {}",token);
//
//        //Return the created Token
//        return token;
//    }

    public Token create(io.queberry.que.service.Service service, SubService subService, String language, String mobile, Token.Medium medium, String branchKey, Integer priority, String privateCode){

        log.info("in token created");

        Integer nextSequence;
        //get the next sequence number and save the sequence manager
        if(service.isSharedSeq()){
            log.info("in shared seq");
//            Map<String, SharedSequenceManager> sharedSequenceManagerMap = sequenceEngine.getBranchSharedSequenceManager().get(branchKey);
            nextSequence = sequenceEngine.getSharedSeqToken(branchKey,service.getSharedSequence());
        }else{
            log.info("in normal seq");
//            Map<String, SequenceManager> sequenceManagerMap = sequenceEngine.getBranchSequenceManager().get(branchKey);
            nextSequence = sequenceEngine.getToken(branchKey,service);
        }

        //Create a Token with the Service and  next number from the Queue . Counter will be assigned once the token is called

        Token token = new Token(service,subService,nextSequence, Type.DISPENSER,language,mobile,medium,branchKey, priority, privateCode);

        //Save the token
//        token = tokenRepository.save(token);

        //Log the created Token
        log.info("Token created : {}",token.getId());

        //Return the created Token
        return token;
    }

    public Token create(io.queberry.que.service.Service service, String branchKey){

        log.info("in token created");

        Integer nextSequence;
        //get the next sequence number and save the sequence manager
        if(service.isSharedSeq()){
            log.info("in shared seq");
//            Map<String, SharedSequenceManager> sharedSequenceManagerMap = sequenceEngine.getBranchSharedSequenceManager().get(branchKey);
            nextSequence = sequenceEngine.getSharedSeqToken(branchKey,service.getSharedSequence());
        }else{
            log.info("in normal seq");
//            Map<String, SequenceManager> sequenceManagerMap = sequenceEngine.getBranchSequenceManager().get(branchKey);
            nextSequence = sequenceEngine.getToken(branchKey,service);
        }

        //Create a Token with the Service and  next number from the Queue . Counter will be assigned once the token is called

        Token token = new Token(service,null,nextSequence,Type.MEETING,"en","", Token.Medium.SMS, branchKey);

        //Save the token
//        token = tokenRepository.save(token);

        //Log the created Token
        log.info("Token created : {}",token.getId());

        //Return the created Token
        return token;
    }

    /**
     * Mehtod to call the next token from the Queue.
     *  -Process and Complete the previous Tokens if any
     *  -Find the User from the request.
     *  -Find the Employee from the User.
     *  -Find Counter from the Employee.
     *  -Find the Service from the Counter.
     *  -Find the Queue from the Service.
     *
     * @return Token
     */
//    public Token next(CounterAgentRequest request,String user){
    public Token next(CounterAgentRequest request, String branchKey){
//        Employee employee = employeeRepository.findByUsername(user);
        log.info("employee Id {}", request.getEmployee().getId());
        EmployeeDTO employee = request.getEmployee();
        CounterDTO counter = request.getCounter();
        log.info("strategy:{}", request.getQueueStrategy());
        log.info("priority:{}", request.getServicePriority());
        log.info("branch Key {}", branchKey);

        Token token;
        if(employee.isCallAll()){
            log.info("in call all");
//            token = getNextTokenForCounter(counter, employee, request.getQueueStrategy(), request.getServicePriority(),branchKey);
//            token.process();
//            token = tokenRepository.save(token);

            //Return the updated Token
//            return token;
        }else if(employee.isCallNew()){
            log.info("in call new");
            // call only the tokens in scheduled state
            token = getNextTokenForCounterInScheduled(counter, employee, request.getQueueStrategy(), request.getServicePriority(), branchKey);
            if (token != null) {
                token.process();
//                token = tokenRepository.save(token);
            }
            //Return the updated Token
            return token;
        }else if(employee.isCallTransfer()){
            log.info("in call transfer");
            // call only the tokens in transferred state based on service (currently only counter service)
            token = getNextTokenForCounterInTransfer(counter, employee, request.getQueueStrategy(), request.getServicePriority(), branchKey);
            if(token != null) {
                token.process();
//                token = tokenRepository.save(token);
            }
            //Return the updated Token
            return token;
        }
        //Get the next token from the Queue

        return null;
    }


    //    for dp world back office user to call if they are not logged in
    public Token processCounterToken(CounterAgentRequest request){
        Token token;
        //Get all the QueueTokens for the Counter sorted in ASC order by createdAt
        List<CounterToken> counterTokens = counterTokenRepository.findByCounterIdOrderByCreatedAtAsc(request.getCounterId());

        //If there are CounterTokens, process the first Token and return it
        if (counterTokens.size() > 0){
            CounterToken counterToken = counterTokens.get(0);
//            token = tokenRepository.findById(counterToken.getTokenId()).orElse(null);
            token = tokenRepository.findTokenById(counterToken.getTokenId()).orElse(null);
            counterTokenRepository.delete(counterToken);
            return token;
        }

        throw new QueueException("No Token in Queue",HttpStatus.PRECONDITION_FAILED);
    }


    //    public Token getNextTokenForCounterInScheduled(CounterDTO counter, EmployeeDTO employee, QueueConfiguration configuration){
    public Token getNextTokenForCounterInScheduled(CounterDTO counter, EmployeeDTO employee, QueueConfiguration.QueueStrategy queueStrategy, QueueConfiguration.ServicePriority servicePriority, String branchKey){

        Set<String> serviceIds = getServiceIdsBasedOnPriority(servicePriority, counter, employee);
//        log.info("counter service ids: {}", serviceIds);
        //Get all the QueueToken for the ids sorted ASC by createdAt
        List<QueueToken> tokensInQueue = queueTokenRepository.findByBranchAndServiceIdInOrderByPriorityAscCreatedAtAsc(branchKey,serviceIds);
        log.info("token list: {}", tokensInQueue.size());
        //Process the first token whose status is CREATED
        Token token;
        if (!tokensInQueue.isEmpty()){
            log.info("token queue is not empty: {}", tokensInQueue.size());
            for(QueueToken queueToken: tokensInQueue) {
//                log.info("queue token: {}", queueToken);
                token = tokenRepository.findByIdAndStatus(queueToken.getTokenId(), TokenStatus.CREATED).orElse(null);
                if (token != null) {
                    log.info("in process queue token: {}", token.getId());
                    queueTokenRepository.delete(queueToken);
                    return token;
                }
            }
            throw new QueueException("No Token in Queue",HttpStatus.PRECONDITION_FAILED);
        }
        else{
            throw new QueueException("No Token in Queue",HttpStatus.PRECONDITION_FAILED);
        }
    }

    //    public Token getNextTokenForCounterInTransfer(CounterDTO counter, EmployeeDTO employee, QueueConfiguration configuration){
    public Token getNextTokenForCounterInTransfer(CounterDTO counter, EmployeeDTO employee, QueueConfiguration.QueueStrategy queueStrategy, QueueConfiguration.ServicePriority servicePriority, String branchKey){
        Token token = null;

        //Get all the QueueTokens for the Counter sorted in ASC order by createdAt
        // first priority to the token transferred to the counter
        List<CounterToken> counterTokens = counterTokenRepository.findByCounterIdOrderByCreatedAtAsc(counter.getId());
        log.info("size{}", counterTokens.size());
        if (counterTokens.size() > 0){
            log.info("in counter tokens");
            CounterToken counterToken = counterTokens.get(0);
            token = tokenRepository.findByIdAndStatus(counterToken.getTokenId(), TokenStatus.CREATED).orElse(null);
            log.info("token dtls: {}", token.getId());
            counterTokenRepository.delete(counterToken);
        }else {
            Set<String> serviceIds = getServiceIdsBasedOnPriority(servicePriority, counter, employee);

            List<QueueToken> queueTokens = queueTokenRepository.findByBranchAndServiceIdInOrderByPriorityAscCreatedAtAsc(branchKey,serviceIds);
            if (!queueTokens.isEmpty()){
                for(QueueToken queueToken: queueTokens) {
                    log.info("queue token: {}", queueToken.getTokenId());
                    token = tokenRepository.findByIdAndStatus(queueToken.getTokenId(), TokenStatus.PROCESSING).orElse(null);
                    log.info("token dtls: {}", token.getId());
                    if (token != null) {
                        queueTokenRepository.delete(queueToken);
                        return token;
                    }
                }
            }
            else {
                throw new QueueException("No Token in Queue",HttpStatus.PRECONDITION_FAILED);
            }
        }
        counterTokens.clear();
        return token;
    }


    private Set<String> getServiceIdsBasedOnPriority(QueueConfiguration.ServicePriority servicePriority, CounterDTO counter, EmployeeDTO employee) {
        switch (servicePriority){
            case COUNTER:
                return getServiceIdsForCounter(counter);
            case USER:
                return getServiceIdsForEmployee(employee, "default");
            case BOTH:
                log.info("in both");
                Set<String> serviceIds;
                serviceIds = getServiceIdsForEmployee(employee, "default");
                log.info("Emp{}", serviceIds.size());
                Set<String> sids = getServiceIdsForCounter(counter);
                log.info("counter {}", sids.size());
                serviceIds.addAll(sids);
                log.info("total {}", serviceIds.size());
                return serviceIds;
            default:
                log.info("in default");
                return getServiceIdsForCounter(counter);
        }
    }

    private Set<String> getServiceIdsForCounter(CounterDTO counter) {
////        Set<io.queberry.que.service.Service> services = new HashSet<>(0);
//        Set<String> serviceIds = counter.getFirstLevelServicesMeta().stream().map(ServiceDTO::getId).collect(Collectors.toSet());
//        serviceIds.addAll(counter.getSecondLevelServicesMeta().stream().map(ServiceDTO::getId).collect(Collectors.toSet()));
//        serviceIds.addAll(counter.getThirdLevelServicesMeta().stream().map(ServiceDTO::getId).collect(Collectors.toSet()));
//        serviceIds.addAll(counter.getFourthLevelServicesMeta().stream().map(ServiceDTO::getId).collect(Collectors.toSet()));
//        log.info("counter services: {}", services);

//        return services.stream().map(AggregateRoot::getId).collect(Collectors.toSet());
//        return serviceIds;
        return Set.of();
    }

    private Set<String> getServiceIdsForEmployee(EmployeeDTO employee, String level){
        switch (level) {
//            case "first":
//                return employee.getServices().stream().map(ServiceDTO::getId).collect(Collectors.toSet());
//            case "second":
//                return employee.getSecond().stream().map(ServiceDTO::getId).collect(Collectors.toSet());
//            case "third":
//                return employee.getThird().stream().map(ServiceDTO::getId).collect(Collectors.toSet());
//            case "fourth":
//                return employee.getFourth().stream().map(ServiceDTO::getId).collect(Collectors.toSet());
//            default:
//                Set<String> temp = new HashSet<>();
//                temp.addAll(employee.getServices().stream().map(io.queberry.que.service.ServiceDTO::getId).collect(Collectors.toSet()));
//                temp.addAll(employee.getSecond().stream().map(io.queberry.que.service.ServiceDTO::getId).collect(Collectors.toSet()));
//                temp.addAll(employee.getThird().stream().map(io.queberry.que.service.ServiceDTO::getId).collect(Collectors.toSet()));
//                temp.addAll(employee.getFourth().stream().map(ServiceDTO::getId).collect(Collectors.toSet()));
//                return temp;
//        }
        }
        return Set.of();
    }


    /**
     * Method to get the next token from the Queue of the passed service
     * @param counter, employee, configuration
     * @return Token
     */
//    public Token getNextTokenForCounter(CounterDTO counter, EmployeeDTO employee, QueueConfiguration configuration){
//    public Token getNextTokenForCounter(CounterDTO counter, EmployeeDTO employee, QueueConfiguration.QueueStrategy queueStrategy, QueueConfiguration.ServicePriority servicePriority, String branchKey){
//
//        Token token;
//        //Get all the QueueTokens for the Counter sorted in ASC order by createdAt
//        List<CounterToken> counterTokens = counterTokenRepository.findByCounterIdOrEmployeeIdOrderByCreatedAtAsc(counter.getId(), employee.getId());
//        log.info("counter tokens size: {}, next token for counter {}",counterTokens, counter.getId());
//        //If there are CounterTokens, process the first Token and return it
//        if (counterTokens.size() > 0){
//            CounterToken counterToken = counterTokens.get(0);
////            token = tokenRepository.findById(counterToken.getTokenId()).orElse(null);
//            token = tokenRepository.findTokenById(counterToken.getTokenId()).orElse(null);
//            log.info("token dtls: {}", token.getId());
//            counterTokenRepository.delete(counterToken);
//            return token;
//        }
//
//        //If the queue config in home is set to service priority
//        if(queueStrategy.equals(QueueConfiguration.QueueStrategy.SERVICE_PRIORITY )){
//            switch (servicePriority){
//                case COUNTER:
//                    log.info("counter service priority");
//                    return servicePriorityToken(counter, branchKey);
//                case USER:
//                    log.info("USER service priority");
//                    return servicePriorityTokenForUserServices(employee, branchKey);
//                case BOTH:
//                    log.info("BOTH service priority");
//                    return bothPriorityToken(counter, employee, branchKey); // change this method
//            }
//        }
//        else{
//            log.info("fifo");
//            // even for fifo the services are fetched based on priority
//            Set<String> serviceIds = getServiceIdsBasedOnPriority(servicePriority, counter, employee);
//
//            log.info("counter service ids: {}", serviceIds.size());
//            //Get all the QueueToken for the ids sorted ASC by createdAt
//            List<QueueToken> tokensInQueue = queueTokenRepository.findByBranchAndServiceIdInOrderByPriorityAscCreatedAtAsc(branchKey, serviceIds);
//            log.info("token list: {}", tokensInQueue.size());
//            //Process the first Token
//            if (!tokensInQueue.isEmpty()){
//                log.info("token queue is not empty: {}", tokensInQueue.get(0));
//                return processQueueToken(tokensInQueue.get(0));
//            }
//        }
//        throw new QueueException("No Token in Queue",HttpStatus.PRECONDITION_FAILED);
//    }

    /**
     * Method to process a QueueToken and return a processed Token
     * @param queueToken queuetoken
     * @return Token
     */
    public Token processQueueToken(QueueToken queueToken){
//        Token token = tokenRepository.findById(queueToken.getTokenId()).orElse(null);
        Token token = tokenRepository.findTokenById(queueToken.getTokenId()).orElse(null);
        log.info("in process queue token: {}",token.getId());
        queueTokenRepository.delete(queueToken);
        return token;
    }

    public Token servicePriorityTokenForUserServices(EmployeeDTO employee, String branchKey){
        if(employee.getServices().size() > 0) {
            Set<String> serviceIds = getServiceIdsForEmployee(employee, "default");
            QueueToken queueToken = findQueueTokenFromServices(serviceIds, branchKey);
            if (queueToken != null)
                return processQueueToken(queueToken);
        }
        return null;
    }

    public Token processAppointmentToken(AppointmentToken appointmentToken){
//        Token token = tokenRepository.findById(appointmentToken.getTokenId()).orElse(null);
        Token token = tokenRepository.findTokenById(appointmentToken.getTokenId()).orElse(null);
        log.info("in process queue token: {}",token.getId());
        appointmentTokenRepository.delete(appointmentToken);
        return token;
    }

    public void processCounterQueue(CounterAgentRequest counterAgentRequest, String branchKey){
        Optional<QueueCounter> qc = queueCounterRepository.findByCounterId(counterAgentRequest.getCounterId()).stream().findFirst();
        if(qc.isPresent()){
            queueCounterRepository.delete(qc.get());
//            messagingTemplate.send("/notifications/" + branchKey + "/counterStatus", new EmployeeRestRepositoryController.EmployeeStatusWrapper(counterAgentRequest.getEmployeeId(), counterAgentRequest.getCounterId(),"OPEN",null));
            counterStatusService.setStatus(counterAgentRequest.getCounterId(),"OPEN");
        }
    }

    public Token servicePriorityToken(CounterDTO counter, String branchKey){
//        if (counter.getFirstLevelServicesMeta().size() > 0){
//            Set<String> serviceIds = counter.getFirstLevelServicesMeta().stream().map(ServiceDTO::getId).collect(Collectors.toSet());
//            QueueToken queueToken = findQueueTokenFromServices(serviceIds, branchKey);
//            if (queueToken != null)
//                return processQueueToken(queueToken);
//        }

//        if (counter.getSecondLevelServicesMeta().size() > 0){
//            Set<String> serviceIds = counter.getSecondLevelServicesMeta().stream().map(ServiceDTO::getId).collect(Collectors.toSet());
//            QueueToken queueToken = findQueueTokenFromServices(serviceIds, branchKey);
//            if (queueToken != null)
//                return processQueueToken(queueToken);
//        }

//        if (counter.getThirdLevelServicesMeta().size() > 0){
//            Set<String> serviceIds = counter.getThirdLevelServicesMeta().stream().map(ServiceDTO::getId).collect(Collectors.toSet());
//            QueueToken queueToken = findQueueTokenFromServices(serviceIds, branchKey);
//            if (queueToken != null)
//                return processQueueToken(queueToken);
//        }

//        if (counter.getFourthLevelServicesMeta().size() > 0){
//            Set<String> serviceIds = counter.getFourthLevelServicesMeta().stream().map(ServiceDTO::getId).collect(Collectors.toSet());
//            QueueToken queueToken = findQueueTokenFromServices(serviceIds, branchKey);
//            if (queueToken != null)
//                return processQueueToken(queueToken);
//        }
        return null;
    }

    public Token bothPriorityToken(CounterDTO counter, EmployeeDTO employee, String branchKey){
        // in both user services are given priority then counter
        if(employee.getServices().size() > 0) { // checking employee first level services
            Set<String> serviceIds = getServiceIdsForEmployee(employee, "first");
//            log.info("service ids {}", serviceIds.size());
            QueueToken queueToken = findQueueTokenFromServices(serviceIds, branchKey);
            if (queueToken != null)
                return processQueueToken(queueToken);
        }

//        if (counter.getFirstLevelServicesMeta().size() > 0){ // checking particular counter services
//            Set<String> serviceIds = counter.getFirstLevelServicesMeta().stream().map(ServiceDTO::getId).collect(Collectors.toSet());
//            QueueToken queueToken = findQueueTokenFromServices(serviceIds, branchKey);
//            if (queueToken != null)
//                return processQueueToken(queueToken);
//        }

        if(employee.getSecond().size() > 0) { // checking employee first level services
            Set<String> serviceIds = getServiceIdsForEmployee(employee, "second");
//            log.info("service ids {}", serviceIds);
            QueueToken queueToken = findQueueTokenFromServices(serviceIds, branchKey);
            if (queueToken != null)
                return processQueueToken(queueToken);
        }

//        if (counter.getSecondLevelServicesMeta().size() > 0){
//            Set<String> serviceIds = counter.getSecondLevelServicesMeta().stream().map(ServiceDTO::getId).collect(Collectors.toSet());
//            QueueToken queueToken = findQueueTokenFromServices(serviceIds, branchKey);
//            if (queueToken != null)
//                return processQueueToken(queueToken);
//        }

        if(employee.getThird().size() > 0) { // checking employee first level services
            Set<String> serviceIds = getServiceIdsForEmployee(employee, "third");
//            log.info("service ids {}", serviceIds);
            QueueToken queueToken = findQueueTokenFromServices(serviceIds, branchKey);
            if (queueToken != null)
                return processQueueToken(queueToken);
        }

//        if (counter.getThirdLevelServicesMeta().size() > 0){
//            Set<String> serviceIds = counter.getThirdLevelServicesMeta().stream().map(ServiceDTO::getId).collect(Collectors.toSet());
//            QueueToken queueToken = findQueueTokenFromServices(serviceIds, branchKey);
//            if (queueToken != null)
//                return processQueueToken(queueToken);
//        }

        if(employee.getFourth().size() > 0) { // checking employee first level services
            Set<String> serviceIds = getServiceIdsForEmployee(employee, "fourth");
//            log.info("service ids {}", serviceIds);
            QueueToken queueToken = findQueueTokenFromServices(serviceIds, branchKey);
            if (queueToken != null)
                return processQueueToken(queueToken);
        }

//        if (counter.getFourthLevelServicesMeta().size() > 0){
//            Set<String> serviceIds = counter.getFourthLevelServicesMeta().stream().map(ServiceDTO::getId).collect(Collectors.toSet());
//            QueueToken queueToken = findQueueTokenFromServices(serviceIds, branchKey);
//            if (queueToken != null)
//                return processQueueToken(queueToken);
//        }
        return null;
    }

    private AppointmentToken findAppointmentTokenFromServices(Set<String> ids){
        Set<AppointmentToken> appointmentTokens = appointmentTokenRepository.findByServiceIdInOrderByAppointmentAtAsc(ids);
        AppointmentToken appointmentToken = null;

        if (!appointmentTokens.isEmpty())
            appointmentToken = appointmentTokens.stream().findFirst().get();

        return appointmentToken;
    }

    private QueueToken findQueueTokenFromServices(Set<String> ids, String branchKey){

        List<QueueToken> queueTokens = queueTokenRepository.findByBranchAndServiceIdInOrderByPriorityAscCreatedAtAsc(branchKey, ids);

        QueueToken queueToken = null;

        if (queueTokens.size() > 0)
            queueToken = queueTokens.get(0);

        return queueToken;
    }

    /**
     * Event listener that Handles @Token.TokenCreated event
     * @param tokenCreated
     */
//    @Async
    @TransactionalEventListener(fallbackExecution = false)
    public void tokenCreated(Token.TokenCreated tokenCreated){
        log.info("{} created",tokenCreated.getToken());
    }


    /**
     * Event listener that Handles @Token.TokenCompleted event
     * @param tokenCompleted
     */
//    @Async
    @TransactionalEventListener(fallbackExecution = true)
    public void tokenCompleted(Token.TokenCompleted tokenCompleted){
        log.info("{} completed",tokenCompleted.getToken());
    }
}

