package io.queberry.que.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.queberry.que.appointment.Appointment;
import io.queberry.que.assistance.Assistance;
import io.queberry.que.enums.Status;
import io.queberry.que.enums.Type;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Getter
@NoArgsConstructor(force = true,access = AccessLevel.PRIVATE)
public class Report {

    @JsonIgnore
    private Set<Assistance> assistances = new HashSet<>(0);
    private Double avgWaitTime;
    private Double avgServeTime;
    private Integer totalTokens;
    private Integer noOfWaiting;
    private Integer totalCompleted;
    private Integer noOfOngoing;
    private Integer noOfTransffered;
    private Integer noOfParked;
    private Integer noOfNoShow;
    private Integer noOfExpired;
    private Type type;
    private LocalDate from;
    private LocalDate to;
    private String party;
    @Setter
    private Integer waitingSla;
    @Setter
    private Integer servingSla;
    private Long waitTimeInQueue;
    private Double avgWaitTimeInQueue;
    private Long totalTimeCompleted;
    private Double avgWaitTimeCompleted;
    private Long idleTime=0L;
    private Long totalTime =0L;
    private Long serveTime;
    private Long breakTime;
    private int breakCount;
    private String employ;
    private String counterName;
    private String serviceName;
    private String token;
    private String tknStatus;
    private LocalDateTime tknCreated;
    private LocalDateTime tknCompletedAt;
    private String name;
    private int age;
    private String gender;
    private String mobile;
    private String accountId;
    private String accountName;
    private Set<String> bookingIds;
    private String accountNo;
    private String emailId;
    public Report(Set<Assistance> assistances, LocalDate from, LocalDate to, Type type, String party) {
        this.assistances = assistances;
        this.from = from;
        this.to = to;
        this.type = type;
        this.party = party;
        if(assistances.size()>0){
            this.totalTokens = findTotalTokens();
            this.noOfWaiting = findNoOfWaiting();
            this.totalCompleted = findNoOfCompleted();
            this.serveTime = findServeTime();
            this.avgServeTime = findAvgServingTime();
            this.avgWaitTime = findAvgWaitingTime();
            this.noOfTransffered = findNoOfTransffered();
            this.noOfParked = findNoOfParked();
            this.noOfNoShow = findNoOfNoShow();
            this.noOfExpired = findNoOfExpired();
            this.noOfOngoing = findNoOfOngoing();
            this.waitTimeInQueue = findWaitTimeInQue();
            this.avgWaitTimeInQueue = findAvgWaitTimeInQue();
            this.totalTimeCompleted = findTotalTimeCompleted();
            this.avgWaitTimeCompleted = findAvgWaitTimeCompleted();
        } else {
            this.totalTokens = 0;
            this.noOfWaiting = 0;
            this.totalCompleted = 0;
            this.serveTime = 0L;
            this.avgServeTime = 0.0;
            this.avgWaitTime = 0.0;
            this.noOfTransffered = 0;
            this.noOfParked = 0;
            this.noOfNoShow = 0;
            this.noOfExpired = 0;
            this.noOfOngoing = 0;
            this.waitTimeInQueue = 0L;
            this.avgWaitTimeInQueue = 0.0;
            this.totalTimeCompleted = 0L;
            this.avgWaitTimeCompleted = 0.0;
        }
    }
    public Report(Set<Assistance> assistances,LocalDate from, LocalDate to, Type type,String party, Long totalTime, Long breakTime, int breakCount) {
        this.assistances = assistances;
        this.from = from;
        this.to = to;
        this.type = type;
        this.party = party;
        if(assistances.size() > 0) {
            this.totalTokens = findTotalTokens();
            this.noOfWaiting = findNoOfWaiting();
            this.totalCompleted = findNoOfCompleted();
            this.serveTime = findServeTime();
            this.avgServeTime = findAvgServingTime();
            this.avgWaitTime = findAvgWaitingTime();
            this.noOfTransffered = findNoOfTransffered();
            this.noOfParked = findNoOfParked();
            this.noOfNoShow = findNoOfNoShow();
            this.noOfExpired = findNoOfExpired();
            this.noOfOngoing = findNoOfOngoing();
            this.waitTimeInQueue = findWaitTimeInQue();
            this.avgWaitTimeInQueue = findAvgWaitTimeInQue();
            this.totalTimeCompleted = findTotalTimeCompleted();
            this.avgWaitTimeCompleted = findAvgWaitTimeCompleted();
        } else {
            this.totalTokens = 0;
            this.noOfWaiting = 0;
            this.totalCompleted = 0;
            this.serveTime = 0L;
            this.avgServeTime = 0.0;
            this.avgWaitTime = 0.0;
            this.noOfTransffered = 0;
            this.noOfParked = 0;
            this.noOfNoShow = 0;
            this.noOfExpired = 0;
            this.noOfOngoing = 0;
            this.waitTimeInQueue = 0L;
            this.avgWaitTimeInQueue = 0.0;
            this.totalTimeCompleted = 0L;
            this.avgWaitTimeCompleted = 0.0;
        }
        this.totalTime = totalTime;
        if(totalTime != 0L) {
            this.idleTime = totalTime - this.serveTime;
        }
        this.breakTime = breakTime;
        this.breakCount = breakCount;
    }
    public Report(Assistance assistance) {
        this.name = assistance.getName();
        this.age = assistance.getAge();
        this.gender = assistance.getGender() != null ? assistance.getGender().name():"";
        this.token = assistance.getTokenRef();
        this.mobile = assistance.getMobile();
        this.accountId = assistance.getAccountId();
        this.accountName = assistance.getAccountName();
        this.accountNo = assistance.getAccountNo();
        this.emailId = assistance.getEmail();
        this.bookingIds = assistance.getBookingIds();
        this.tknStatus = assistance.getStatus().name();
        this.serveTime = (assistance.getStatus().equals(Status.SCHEDULED) || assistance.getStatus().equals(Status.EXPIRED)) ? 0L: assistance.getTotalServeTime();
        this.waitTimeInQueue = assistance.getWaitingTime();
        this.serviceName = assistance.getService().getName();
        if (assistance.findLastSession() != null) {
            this.counterName = assistance.findLastSession().getCounter() != null ? assistance.findLastSession().getCounter().getName() : "";
            this.employ = assistance.findLastSession().getEmployee();
            this.tknCompletedAt = assistance.findLastSession().getCompletedAt();
        }
        else {
            this.counterName = "";
            this.employ = "";
            this.tknCompletedAt = null;
        }
        this.tknCreated = assistance.getCreatedAt();
    }

    private Integer findTotalTokens() {
        Set<Assistance> aas = this.assistances.stream().filter(assistance -> assistance.getAppointment() != null && assistance.getAppointment().getSchedulingType().equals(Appointment.SchedulingType.GROUP)).collect(Collectors.toSet());
        int grpCnt=0;
        for(Assistance assistance: aas){
            grpCnt += assistance.getAppointment().getNoOfAppointments();
        }
        return (this.assistances.size() - aas.size()) + grpCnt;
    }
    private Integer findNoOfWaiting(){
        return this.assistances.stream().filter(assistance -> assistance.getStatus() == Status.SCHEDULED).collect(Collectors.toSet()).size();
    }
    private Double findAvgWaitTimeInQue(){
        if (noOfWaiting == 0L)
            return 0.0;
        return  Math.ceil(waitTimeInQueue/noOfWaiting);
    }
    private Long findWaitTimeInQue(){
        if (noOfWaiting == 0L)
            return 0L;
        Stream<Assistance> assistanceSet = assistances.stream().filter(assistance -> assistance.getStatus() == Status.SCHEDULED);
        Long total = assistanceSet.map(Assistance::getWaitingTime).reduce(Long::sum).orElse(0L);
        return total;
    }
    private Long findServeTime(){
        if (totalCompleted == 0L)
            return 0L;
        Long total = assistances.stream().map(Assistance::getTotalServeTime)
                .reduce(Long::sum).orElse(0L);
        return total;
    }
    private Double findAvgServingTime(){
        if (totalCompleted == 0L)
            return 0.0;
        return Math.ceil(this.serveTime/totalCompleted);
    }
    private Double findAvgWaitingTime(){
        if (totalTokens == 0L)
            return 0.0;
        Long total = assistances.stream().filter(assistance -> assistance.getStatus() != Status.EXPIRED).map(Assistance::getWaitingTime)
                .reduce(Long::sum).orElse(0L);
        return  Math.ceil((double) total /totalTokens);
    }
    private Long findTotalTimeCompleted(){
        if(totalCompleted == 0L)
            return 0L;
        Stream<Assistance> assistanceSet = assistances.stream().filter(assistance -> assistance.getStatus() == Status.COMPLETED || assistance.getStatus() == Status.NO_SHOW);
//        log.info("assistance set info {}", assistanceSet);
        Long total = assistanceSet.map(Assistance::getWaitingTime).reduce(Long::sum).orElse(0L);
        return total;
    }
    private Double findAvgWaitTimeCompleted() {
        if (totalCompleted == 0L)
            return 0.0;
//        Stream<Assistance> assistanceSet = assistances.stream().filter(assistance -> assistance.getStatus() == Status.COMPLETED || assistance.getStatus() == Status.NO_SHOW);
//        Stream<Assistance> assistanceSet = assistances.stream().filter(assistance -> assistance.getStatus() == Status.COMPLETED);
//        Long total = assistanceSet.map(Assistance::getWaitingTime).reduce(Long::sum).orElse(0L);
        return Math.ceil((double) this.totalTimeCompleted /totalCompleted);
    }
    private Integer findNoOfTransffered(){
        return (int) assistances.stream().filter(Assistance::isTransffered).count();
    }
    private Integer findNoOfParked(){
        return (int) assistances.stream().filter(Assistance::isParked).count();
    }
    private Integer findNoOfNoShow(){
        return (int) assistances.stream().filter(Assistance::isNoShow).count();
    }
    private Integer findNoOfExpired(){
        return (int) assistances.stream().filter(Assistance::isExpired).count();
    }
    private Integer findNoOfCompleted(){
        return (int) assistances.stream().filter(Assistance::isCompleted).count();
    }
    private Integer findNoOfOngoing(){
        return (int) assistances.stream().filter(Assistance::isOngoing).count();
    }

    /*public Long waitingSla(){
        if (waitingSla == null || waitingSla == 0L)
            return 0L;
        return ((getAvgWaitTime()/(waitingSla*60))*100);
    }

    public Long waitingSlaDiff(){
        if (waitingSla == null || waitingSla == 0L)
            return 0L;
        return 100-waitingSla();
    }

    public Long servingSla(){
        if (servingSla == null || servingSla == 0L)
            return 0L;
        return ((getAvgServeTime()/(servingSla*60))*100);
    }

    public Long servingSlaDiff(){
        if (servingSla == null || servingSla == 0L)
            return 0L;
        return 100-servingSla();
    }*/
    public Integer totalServedInSla(){
        if (totalTokens == 0)
            return 0;
        return assistances.stream().filter(assistance -> assistance.getStatus().equals(Status.COMPLETED) && assistance.getTotalServeTime() <= servingSla*60).collect(Collectors.toSet()).size();
    }
    public Integer totalServedOutsideSla(){
        if (totalTokens == 0)
            return 0;
       return totalCompleted - totalServedInSla();
    }
    public Integer totalServedInSlaPercentage(){
        if (totalTokens == 0)
            return 0;
        return (int)((float)totalServedInSla()/(float)totalTokens * 100);
    }
    public Integer totalServedOutsideSlaPercentage(){
        if (totalTokens == 0)
            return 0;
        return 100 - totalServedInSlaPercentage();
    }
    public Integer totalWaitingInSla(){
        if (totalTokens == 0)
            return 0;
        return assistances.stream().filter(assistance -> assistance.getWaitingTime() <= waitingSla*60).collect(Collectors.toSet()).size();
    }
    public Integer totalWaitingOutsideSla(){
        if (totalTokens == 0)
            return 0;
        return totalTokens - totalWaitingInSla();
    }
    public Integer totalWaitingInSlaPercentage(){
        if (totalTokens == 0)
            return 0;
        return (int)((float)totalWaitingInSla()/(float)totalTokens * 100);

    }
    public Integer totalWaitingOutsideSlaPercentage(){
        if (totalTokens == 0)
            return 0;
        return 100 - totalWaitingInSlaPercentage();
    }
}
