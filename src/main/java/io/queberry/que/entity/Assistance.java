package io.queberry.que.entity;

import io.queberry.que.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

@Entity(name = "que_assistance")
@Table(name = "que_assistance", indexes = {@Index(name="br_index",columnList = "branch"),
        @Index(name="dt_index",columnList = "createdAt"),
        @Index(name="idx_branch_created_at", columnList="createdAt, branch"),
        @Index(name="idx_createdat_status_branch_service",columnList = "createdAt,entity_status,branch,service_id"),
        @Index(name="idx_createdat_service", columnList = "createdAt,service_id")})
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "AssistanceCache")
//@FilterDef(name = "todayFilter", parameters = @ParamDef(name = "minDate", type = "java.time.LocalDate"))
//@Filter(name = "todayFilter", condition = "DATE(created_at) = :minDate")
@Getter
@Setter
//@ToString
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@EqualsAndHashCode(callSuper = true,of = "createdAt")
public class Assistance extends AggregateRoot<Assistance> {

    @Column(unique = true, length = 50)
    private String tokenRef;

    @ManyToOne
    private Service service;

    private Integer number;

//    @ManyToOne
//    private SubService subService;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Status> journey = new TreeMap();

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime servingStart;
//
    @Column(name = "entity_status", length = 30)
    @Enumerated(EnumType.STRING)
    private Status status;

//    @Column(name = "branch_id")
//    private String branch;

    private String branch;
//
//    @Column(name = "assistance_type",length = 30)
//    @Enumerated(EnumType.STRING)
//    private Type type;
//
//    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
//    private Set<Session> sessions = new HashSet<>(0);
//
//    @Column(length = 20)
//    private String language;
//    @Column(length = 50)
//    private String mobile;
//
//    @Enumerated(EnumType.STRING)
//    @Column(length = 20)
//    private Token.Medium medium;
//
//    @OneToOne
//    private Appointment appointment;
//
//    private String name;
//
//    @Column(length = 100)
//    private String email;
//
//    private int age;
//
//    @Enumerated(EnumType.STRING)
//    @Column(length = 20)
//    private Gender gender;
//
//    private int syncdb = 0;
//
//    private boolean hasAppointment = false;
//    @Column(length = 50)
//    private String branch;
//    @Column(length = 50)
//    private String accountId;
//    private String accountName;
//    @Column(length = 50)
//    private String accountNo;
//
//    @ElementCollection(fetch = FetchType.EAGER)
//    private Set<String> bookingIds;
//    @Column(length = 50)
//    private String eventId;
//    @Column(length = 50)
//    private String cardNo;
//    @Column(length = 20)
//    private String dob;
//
//    @Column(length = 50)
//    private String emiratesId;
//
//    @Column(length = 50)
//    @Getter(AccessLevel.NONE)
//    private String eidphone;
//
//    @Column(length = 100)
//    private String nationality;
//    @Column(length = 100)
//    private String occupation;
//    @Column(name = "eid_scanned", nullable = false, columnDefinition = "bit default 0")
//    private boolean eidScanned;
//
//    private String privateCode;
//
////    public String getToken(){
////        if(number == null || service == null)
////            return null;
////        return getString(service, number, privateCode); // importing this method from token.java
////    }
//
//
//    public Optional<Long> getTotalServeTimeInSeconds(){
//        return sessions.stream().map(Session::getServeTime).reduce(Long::sum);
//    }
//
//    public Long getWaitingTime(){
//        if (servingStart == null)
//            return Duration.between(createdAt,LocalDateTime.now()).getSeconds();
//        return Duration.between(createdAt,servingStart).getSeconds();
//
//       /* if (servingStart == null)
//            return 0L;
//        return Duration.between(createdAt,servingStart).getSeconds();*/
//    }
//
//    public Assistance(Token token){
//        log.info("assistance creation");
//        this.tokenRef = token.getId();
//        this.service = token.getService();
//        this.status = Status.SCHEDULED;
//        journey.put(createdAt.toString(),status);
//        this.number = token.getNumber();
//        this.language = token.getLanguage();
//        this.type = Type.valueOf(token.getType().name());
//        this.mobile = token.getMobile();
//        this.medium = token.getMedium();
//        this.branch = token.getBranch();
////        if (token.getType().equals(io.queberry.que.token.Type.APPOINTMENT))
//        if (token.getAppointment() != null)
//            this.appointment = token.getAppointment();
//        this.registerEvent(AssistanceCreated.of(this));
//    }
//
//    public Assistance(Token token, DispenserController.TokenRequest tokenreq) throws Exception {
//        log.info("assistance creation");
//        this.tokenRef = token.getId();
//        this.service = token.getService();
//        this.status = Status.SCHEDULED;
//        journey.put(createdAt.toString(),status);
//        this.number = token.getNumber();
//        this.language = token.getLanguage();
//        this.type = Type.valueOf(token.getType().name());
//        this.mobile = token.getMobile();
//        this.medium = token.getMedium();
//        this.branch = token.getBranch();
//        this.privateCode = token.getPrivateCode();
//        if (tokenreq.getName() != null){
//            this.name = tokenreq.getName();
//        }
//        if(tokenreq.getGender() != null){
//            this.gender = tokenreq.getGender();
//        }
//        if(tokenreq.getCardNo() != null){
//            this.cardNo = EncryptionUtil.encryptData(tokenreq.getCardNo());
//        }
//        if (token.getAppointment() != null) {
//            this.appointment = token.getAppointment();
//        }
//        if (tokenreq.getBookinIds() != null) {
//            this.bookingIds = tokenreq.getBookinIds();
//        }
//        if(tokenreq.getDob() != null){
//            this.dob = tokenreq.getDob();
//        }
//        if(tokenreq.getEidScanned() != null){
//            this.eidScanned = tokenreq.getEidScanned();
//        }
//        if(tokenreq.getEmiratesId() != null){
////            this.emiratesId = tokenreq.getEmiratesId();
//            this.emiratesId = EncryptionUtil.encryptData(tokenreq.getEmiratesId());
//        }
//        if(tokenreq.getEidphone() != null){
//            this.eidphone = EncryptionUtil.encryptData(tokenreq.getEidphone());
//        }
//        if(tokenreq.getNationality() != null){
//            this.nationality = tokenreq.getNationality();
//        }
//        if(tokenreq.getOccupation() != null){
//            this.occupation = tokenreq.getOccupation();
//        }
//        if(tokenreq.getEmail() != null){
//            this.email = EncryptionUtil.encryptData(tokenreq.getEmail());
//        }
//        if(tokenreq.getAge() != null){
//            this.age = tokenreq.getAge();
//        }
//        if (tokenreq.getBookinIds() != null) {
//            this.bookingIds = tokenreq.getBookinIds();
//        }
//        if(tokenreq.getAccountId() != null) {
//            this.accountId = tokenreq.getAccountId();
//        }
//        if(tokenreq.getAccountName() != null) {
//            this.accountName = tokenreq.getAccountName();
//        }
//        this.registerEvent(AssistanceCreated.of(this));
//    }
//
//
//    public Assistance(Service service, SubService subService, Status status, Type type) {
//        this.service = service;
////        this.subService = subService;
//        this.status = status;
//        this.type = type;
//    }
//
//    public static Assistance walkin(Service service, SubService subService, Counter counter, String employee,Token token){
//        Assistance assistance = new Assistance(service,subService,Status.ATTENDING,Type.WALKIN);
//        assistance.tokenRef = token.getId();
//        assistance.number = token.getNumber();
//        Session session = new Session(service, counter, employee);
//        assistance.sessions.add(session);
//        assistance.tokenRef = token.getId();
//        assistance.branch = token.getBranch();
//        assistance.servingStart = LocalDateTime.now();
//        assistance.bookingIds = new HashSet<>();
//        assistance.privateCode = token.getPrivateCode();
//        return assistance.andEvent(AssistanceCalled.of(assistance));
//    }
//
//    public Assistance call(Counter counter, String employee){
//        Service service = null;
//        Session session = findLastSession();
//        log.info("in assistance java call:{}", this.service);
//        if (session!= null && session.getStatus().equals(Status.TRANSFERRED_TO_SERVICE))
//            service = session.getTransferredToService();
//        else if(session!= null && session.getStatus().equals(Status.TRANSFERRED_TO_COUNTER))
//            service = session.getService();
//        else
//            service = this.service;
//        log.info("sevice: {}", service);
//        if (sessions.size() == 0)
//            servingStart = LocalDateTime.now();
//        this.sessions.add(new Session(service,counter,employee));
//        this.status = Status.ATTENDING;
//        journey.put(LocalDateTime.now().toString(),status);
//        return andEvent(AssistanceCalled.of(this));
//    }
//
//    public Assistance meeting(String name, String mobile, String email, String employee){
//        this.mobile = mobile;
//        this.name = name;
//        this.email = email;
//        if (sessions.size() == 0)
//            servingStart = LocalDateTime.now();
//        this.sessions.add(new Session(service,null,employee));
//        this.status = Status.ATTENDING;
//        journey.put(LocalDateTime.now().toString(),status);
//        return andEvent(MeetingAssistanceCalled.of(this));
//    }
//
//    public Assistance greeter(String id){
//        // when transferring the same token multiple times from greeter multiple attending sessions are created
//        // So moved the logic to sessions.size condition
//        if (sessions.size() == 0) {
//            servingStart = LocalDateTime.now();
//        }
//        this.sessions.add(new Session(service,null,id));
//        this.status = Status.ATTENDING;
//        journey.put(LocalDateTime.now().toString(),status);
//
////        else{
////            Session lastSession = findLastSession();
////            Service service = lastSession.getService();
////            this.status = Status.ATTENDING;
////            this.sessions.add(new Session(service,null,id));
////            journey.put(LocalDateTime.now().toString(),status);
////        }
//        return this;
//    }
//
//
//    public Assistance announce(){
//        return  andEvent(AssistanceCalled.of(this));
//    }
//
//    public Assistance noShow(){
//        if(this.getStatus() == Status.SCHEDULED || this.getStatus() == Status.ATTENDING){
//            this.status = Status.NO_SHOW;
//            Session session = getOngoingSession();
//            if (session != null)
//                session.noShow();
//            journey.put(LocalDateTime.now().toString(),status);
//            return andEvent(AssistanceNoShowEvent.of(this));
//        }
//        throw new QueueException("Assistance not in Scheduled or Attending state to mark as No Show",HttpStatus.PRECONDITION_FAILED);
//    }
//
//    public Assistance complete(SubTransaction subService, String remarks, String subServiceData){
//        log.info("assistance complete");
//        if (this.status == Status.COMPLETED)
//            throw new QueueException("Assistance is already completed",HttpStatus.PRECONDITION_FAILED);
//
//        if (this.status != Status.ATTENDING)
//            throw new QueueException("Assistance is not in Attending state to Complete",HttpStatus.PRECONDITION_FAILED);
//
//        this.status = Status.COMPLETED;
//        getOngoingSession().complete(subService, remarks, subServiceData );
//        journey.put(LocalDateTime.now().toString(),status);
//        return andEvent(AssistanceCompleted.of(this));
//    }
//
//    public Assistance sync(Assistance asst){
//        return andEvent(AssistanceCompleted.of(asst));
//    }
//
//    public Assistance hold(String remarks){
//        this.status = Status.HOLD;
//        getOngoingSession().hold(remarks);
//        journey.put(LocalDateTime.now().toString(),status);
//        return andEvent(AssistanceOnHold.of(this));
//    }
//
////    public Assistance recall(){
////        Session session = findLastSession();
////        Service service = session.getService();
////        this.status = Status.ATTENDING;
////        Session lastSession = findLastSession();
////        this.sessions.add(new Session(service,lastSession.getCounter(),lastSession.getEmployee()));
////        journey.put(LocalDateTime.now().toString(),status);
////        return  andEvent(AssistanceCalled.of(this));
////    }
//
//    public Assistance recall(String empId){
////        Session session = findLastSession();
//        Session lastSession = findLastSession();
////        this.sessions.add(new Session(service,lastSession.getCounter(),lastSession.getEmployee()));
//        Service service = lastSession.getService();
//        this.status = Status.ATTENDING;
////        Session lastSession = findLastSession();
//        this.sessions.add(new Session(service,lastSession.getCounter(),empId));
//        journey.put(LocalDateTime.now().toString(),status);
//        return andEvent(AssistanceCalled.of(this));
//    }
//
//    public Assistance transferToCounter(Counter counter){
//        this.status = Status.TRANSFERRED_TO_COUNTER;
//        getOngoingSession().transferToCounter(counter);
//        journey.put(LocalDateTime.now().toString(),status);
//        return andEvent(AssistanceTransferedToCounter.of(this,counter));
//    }
//
//    public Assistance transferToService(Service service){
//        this.status = Status.TRANSFERRED_TO_SERVICE;
//        getOngoingSession().transferToService(service);
//        journey.put(LocalDateTime.now().toString(),status);
//        return andEvent(AssistanceTransferedToService.of(this,service));
//
//    }
//
//    public Assistance transferToUser(Employee employee){
//        this.status = Status.TRANSFERRED_TO_USER;
//        getOngoingSession().transferToUser(employee);
//        journey.put(LocalDateTime.now().toString(),status);
//        return andEvent(AssistanceTransferredToUser.of(this,employee));
//
//    }
//
//    public Assistance expire(){
//        if (getOngoingSession() == null){
//            this.status = Status.EXPIRED;
//            journey.put(LocalDateTime.now().toString(),status);
//            return andEvent(AssistanceExpired.of(this));
//        }
////        else{
////            this.status = Status.COMPLETED;
////            getOngoingSession().complete(subService, "");
////            journey.put(LocalDateTime.now().toString(),status);
////            return andEvent(AssistanceCompleted.of(this));
////        }
//        else{
//            throw new QueueException("Assistance ongoing . Cannot expire. "+getOngoingSession().toString(),HttpStatus.PRECONDITION_FAILED);
//        }
//
//    }
//
//    public Assistance updateDtls(String name, String email, boolean hasAppointment) {
//        this.name = name;
//        this.email = email;
//        this.hasAppointment = hasAppointment;
//        return this;
//    }
//
//    public Assistance updateDtls(String name, String email, boolean hasAppointment,String accountId,String accountName, Set<String> srnos, String accountNo) {
//        this.name = name;
//        this.email = email;
//        this.hasAppointment = hasAppointment;
//        this.accountId = accountId;
//        this.accountName = accountName;
////        this.bookingIds = new ArrayList<>(srnos);
//        this.bookingIds = srnos;
//        this.accountNo = accountNo;
//        return this;
//    }
//
//    public Session getOngoingSession(){
//        return sessions.stream().filter(Session::isOngoing).findFirst().orElse(null);
//    }
//
//    public Session findLastSession(){
//        List<Session> tempSessions = new ArrayList<>(0);
//        tempSessions.addAll(sessions);
//        tempSessions.sort(Comparator.comparing(Session::getCreatedAt));
//        if (tempSessions.size() > 0)
//            return tempSessions.get(tempSessions.size()-1);
//        return null;
//    }
//
//    public Session findSLastSession(){
//        List<Session> tempSessions = new ArrayList<>(0);
//        tempSessions.addAll(sessions);
//        tempSessions.sort(Comparator.comparing(Session::getCreatedAt));
//        if (tempSessions.size() > 1)
//            return tempSessions.get(tempSessions.size()-2);
//        return null;
//    }
//
//    /**
//     * Reporting functions
//     */
//
//    public Long getTotalServeTime(){
//        return sessions.stream().map(Session::getServeTime).reduce(Long::sum).orElse(0L);
//    }
//
//    public boolean isTransffered(){
//        boolean transffered;
//        if (sessions.stream().filter(session -> session.getStatus() == Status.TRANSFERRED_TO_COUNTER || session.getStatus() == Status.TRANSFERRED_TO_SERVICE || session.getStatus() == Status.TRANSFERRED_TO_USER).collect(Collectors.toSet()).size() > 0)
//            transffered = true;
//        else
//            transffered = false;
//        return transffered;
//    }
//
//    public boolean isOngoing(){
//        return this.getStatus() == Status.ATTENDING;
//    }
//
//    public boolean isNoShow(){
//        boolean noShow;
//        if (sessions.stream().filter(session -> session.getStatus() == Status.NO_SHOW).collect(Collectors.toSet()).size() > 0)
//            noShow = true;
//        else
//            noShow = false;
//        return noShow;
//    }
//
//    public boolean isParked(){
//        boolean parked;
//        if (sessions.stream().filter(session -> session.getStatus() == Status.HOLD).collect(Collectors.toSet()).size() > 0)
//            parked = true;
//        else
//            parked = false;
//        return parked;
//    }
//
//    public boolean isCompleted(){
//        if (this.getStatus() == Status.COMPLETED)
//            return true;
//        else
//            return false;
//    }
//
//
//
//    public boolean isExpired(){
//        return this.status == Status.EXPIRED;
//    }
//
//    @Value
//    @EqualsAndHashCode(callSuper = true)
//    @RequiredArgsConstructor(staticName = "of")
//    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//    public static class AssistanceCreated extends DomainEvent<Assistance> implements AssistanceEvent {
//
//        @AggregateReference
//        final Assistance assistance;
//    }
//
//    @Value
//    @EqualsAndHashCode(callSuper = true)
//    @RequiredArgsConstructor(staticName = "of")
//    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//    public static class AssistanceNoShowEvent extends DomainEvent<Assistance> implements AssistanceEvent {
//
//        @AggregateReference
//        final Assistance assistance;
//    }
//
//    @Value
//    @EqualsAndHashCode(callSuper = true)
//    @RequiredArgsConstructor(staticName = "of")
//    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//    public static class AssistanceCalled extends DomainEvent<Assistance> implements AssistanceEvent {
//
//        @AggregateReference
//        final Assistance assistance;
//    }
//
//    @Value
//    @EqualsAndHashCode(callSuper = true)
//    @RequiredArgsConstructor(staticName = "of")
//    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//    public static class MeetingAssistanceCalled extends DomainEvent<Assistance> implements AssistanceEvent {
//
//        @AggregateReference
//        final Assistance assistance;
//    }
//
//    @Value
//    @EqualsAndHashCode(callSuper = true)
//    @RequiredArgsConstructor(staticName = "of")
//    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//    public static class AssistanceCompleted extends DomainEvent<Assistance> implements AssistanceEvent {
//
//        @AggregateReference
//        final Assistance assistance;
//    }
//
//    @Value
//    @EqualsAndHashCode(callSuper = true)
//    @RequiredArgsConstructor(staticName = "of")
//    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//    public static class AssistanceExpired extends DomainEvent<Assistance> implements AssistanceEvent {
//
//        @AggregateReference
//        final Assistance assistance;
//    }
//
//    @Value
//    @EqualsAndHashCode(callSuper = true)
//    @RequiredArgsConstructor(staticName = "of")
//    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//    public static class AssistanceTransferedToCounter extends DomainEvent<Assistance> implements AssistanceEvent {
//
//        @AggregateReference
//        final Assistance assistance;
//
//        @AggregateReference
//        final Counter counter;
//    }
//
//    @Value
//    @EqualsAndHashCode(callSuper = true)
//    @RequiredArgsConstructor(staticName = "of")
//    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//    public static class AssistanceTransferredToUser extends DomainEvent<Assistance> implements AssistanceEvent {
//
//        @AggregateReference
//        final Assistance assistance;
//
//        @AggregateReference
//        final Employee employee;
//    }
//
//    @Value
//    @EqualsAndHashCode(callSuper = true)
//    @RequiredArgsConstructor(staticName = "of")
//    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//    public static class AssistanceTransferedToService extends DomainEvent<Assistance> implements AssistanceEvent {
//
//        @AggregateReference
//        final Assistance assistance;
//
//        @AggregateReference
//        final Service service;
//    }
//
//    @Value
//    @EqualsAndHashCode(callSuper = true)
//    @RequiredArgsConstructor(staticName = "of")
//    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//    public static class AssistanceOnHold extends DomainEvent<Assistance> implements AssistanceEvent {
//
//        @AggregateReference
//        final Assistance assistance;
//    }
//
//    /*public Map<LocalDateTime,Status> getJourney(){
//        return Collections.unmodifiableMap(new HashMap<>(this.journey));
//    }
//
//    public Set<Session> getSessions(){
//        return Collections.unmodifiableSet(new HashSet<>(this.sessions));
//    }
//
//    public String getTokenRef() {
//        return tokenRef;
//    }
//
//    public Service getService() {
//        return service;
//    }
//
//    public SubService getSubService() {
//        return subService;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public LocalDateTime getServingStart() {
//        return servingStart;
//    }
//
//    public Status getStatus() {
//        return status;
//    }
//
//    public Type getType() {
//        return type;
//    }*/
//}
}
