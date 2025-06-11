package io.queberry.que.appointment;

import io.queberry.que.assistance.Assistance;
import io.queberry.que.assistance.AssistanceRepository;
import io.queberry.que.config.Appointment.AppointmentConfiguration;
import io.queberry.que.config.Appointment.AppointmentConfigurationRepository;
import io.queberry.que.config.Appointment.AppointmentService;
import io.queberry.que.config.Redis.RedisSequenceEngine;
import io.queberry.que.config.Tenant.TenantContext;
import io.queberry.que.enums.Type;
import io.queberry.que.exception.QueueException;
import io.queberry.que.queue.AppointmentToken;
import io.queberry.que.queue.AppointmentTokenRepository;
import io.queberry.que.service.ServiceRepository;
import io.queberry.que.token.Token;
import io.queberry.que.token.TokenRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultAppointmentService implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final TokenRepository tokenRepository;
    private final AppointmentTokenRepository appointmentTokenRepository;
    private final MAppointmentListRepository mAppointmentListRepository;
    private final AssistanceRepository assistanceRepository;
    private final ServiceRepository serviceRepository;
    private final AppointmentConfigurationRepository appointmentConfigurationRepository;
    //    private final SequenceEngine sequenceEngine;
    private final RedisSequenceEngine sequenceEngine;

    @Override
    public Token checkin(Appointment appointment, Token.Medium medium) {
        log.info("in appointment generate token");
//        if (!(appointment.getTypeofAppointment().equals(Appointment.TypeofAppointment.WALKIN))){
        appointment = appointment.checkin();
        appointment.setSrnos(appointment.getSrnos());
//            appointment.setDate(appointment.getAppointmentdt().toLocalDate());
//            appointment = appointmentRepository.save(appointment);
//        }
        log.info(appointment.getService() + "    " + appointment.getMobile());
        Token token;
        String branchKey = TenantContext.getBranchKey();
        if (appointment.getKeyAccount().equals(Boolean.TRUE)) {
            String serviceName = appointment.getService().getName();
            log.info("service name {}:", serviceName);
            String searchString = serviceName + "%KA";
            Optional<io.queberry.que.service.Service> service = serviceRepository.findByNameLike(searchString);

            if (service.isPresent()) {
//                Integer nextSequence = sequenceEngine.getToken(TenantContext.getBranchKey(),service.get());
                //get the next sequence number and save the sequence manager
                Integer nextSequence;
                if (service.get().isSharedSeq()) {
                    log.info("in shared seq");
                    nextSequence = sequenceEngine.getSharedSeqToken(branchKey, service.get().getSharedSequence());
                } else {
                    log.info("in normal seq");
                    nextSequence = sequenceEngine.getToken(branchKey, service.get());
                }
                token = new Token(service.get(), null, nextSequence, Type.DISPENSER, "en", appointment.getMobile(), medium, TenantContext.getBranchKey());
            } else {
//                Integer nextSequence = sequenceEngine.getToken(TenantContext.getBranchKey(),appointment.getService());
                Integer nextSequence;
                if (appointment.getService().isSharedSeq()) {
                    log.info("in shared seq");
                    nextSequence = sequenceEngine.getSharedSeqToken(branchKey, appointment.getService().getSharedSequence());
                } else {
                    log.info("in normal seq");
                    nextSequence = sequenceEngine.getToken(branchKey, appointment.getService());
                }
//                token = new Token(appointment.getService(), null, nextSequence, Type.DISPENSER, "en", appointment.getMobile(), medium, TenantContext.getBranchKey());
                token = new Token(appointment.getService(), null, nextSequence, Type.DISPENSER, "en", appointment.getMobile(), medium, appointment.getBranch(), 1, null);
            }
        } else {
//            Map<String, SequenceManager> sequenceManagerMap = sequenceEngine.getBranchSequenceManager().get(appointment.getBranch());
//            Integer nextSequence = sequenceEngine.getToken(TenantContext.getBranchKey(),appointment.getService());
            Integer nextSequence;
            if (appointment.getService().isSharedSeq()) {
                nextSequence = sequenceEngine.getSharedSeqToken(branchKey, appointment.getService().getSharedSequence());
            } else {
                nextSequence = sequenceEngine.getToken(branchKey, appointment.getService());
            }
            token = new Token(appointment.getService(), null, nextSequence, Type.DISPENSER, "en", appointment.getMobile(), medium, appointment.getBranch(), 1, null);
//            token = new Token(appointment.getService(), null,nextSequence, Type.DISPENSER,"en", appointment.getMobile(), medium, appointment.getBranch());
        }

        log.info("token: {}", token);
        token = token.appointment(appointment);
        token = tokenRepository.save(token);
        AppointmentToken appointmentToken = AppointmentToken.of(token);
        appointmentToken = appointmentTokenRepository.save(appointmentToken);
        log.info("Token : {} created for Appointment : {}", token, appointment);

        Assistance assistance = new Assistance(token);
        assistance.setMobile(appointment.getMobile());
//        assistance.updateDtls(appointment.getName(),appointment.getEmail(),true);
        assistance.updateDtls(appointment.getFirstName(), appointment.getEmail(), true, appointment.getAccountId(), appointment.getAccountName(), appointment.getSrnos(), null);
        Assistance assistance1 = assistanceRepository.save(assistance);
        log.info("{} created for {}", assistance1, token);

        appointment.setAssistanceid(assistance1.getId());
        appointment.setToken(token.getId());
//        appointment.setDate(appointment.getAppointmentdt().toLocalDate());
        appointment = appointmentRepository.save(appointment);

        if (!TenantContext.getCurrentTenant().equals("queberry"))
            addMasterAppointment(appointment);
        return token;
    }

    @Override
    public Token checkin(String mobile, Appointment appointment, Token.Medium medium) {
        appointment.checkin();
        appointment.setSrnos(appointment.getSrnos());
//        appointment.setDate(appointment.getAppointmentdt().toLocalDate());
        log.info(appointment.getService() + "    " + appointment.getMobile());
        Token token;
        String branchKey = TenantContext.getBranchKey();
        if (appointment.getKeyAccount().equals(Boolean.TRUE)) {
            String serviceName = appointment.getService().getName();
            log.info("service name {}:", serviceName);
            String searchString = serviceName + "%KA";
            Optional<io.queberry.que.service.Service> service = serviceRepository.findByNameLike(searchString);
            if (service.isPresent()) {
//                Map<String, SequenceManager> sequenceManagerMap = sequenceEngine.getBranchSequenceManager().get(appointment.getBranch());
//                Integer nextSequence = sequenceEngine.getToken(TenantContext.getBranchKey(),service.get());
                Integer nextSequence;
                if (service.get().isSharedSeq()) {
                    log.info("in shared seq");
                    nextSequence = sequenceEngine.getSharedSeqToken(branchKey, service.get().getSharedSequence());
                } else {
                    log.info("in normal seq");
                    nextSequence = sequenceEngine.getToken(branchKey, service.get());
                }
                token = new Token(service.get(), null, nextSequence, Type.DISPENSER, "en", appointment.getMobile(), medium, TenantContext.getBranchKey());
            } else {
//                Map<String, SequenceManager> sequenceManagerMap = sequenceEngine.getBranchSequenceManager().get(appointment.getBranch());
//                Integer nextSequence = sequenceEngine.getToken(TenantContext.getBranchKey(),appointment.getService());
                Integer nextSequence;
                if (appointment.getService().isSharedSeq()) {
                    log.info("in shared seq");
                    nextSequence = sequenceEngine.getSharedSeqToken(branchKey, appointment.getService().getSharedSequence());
                } else {
                    log.info("in normal seq");
                    nextSequence = sequenceEngine.getToken(branchKey, appointment.getService());
                }
                token = new Token(appointment.getService(), null, nextSequence, Type.DISPENSER, "en", appointment.getMobile(), medium, TenantContext.getBranchKey());
            }
        } else {
//            Map<String, SequenceManager> sequenceManagerMap = sequenceEngine.getBranchSequenceManager().get(appointment.getBranch());
//            Integer nextSequence = sequenceEngine.getToken(TenantContext.getBranchKey(),appointment.getService());
            Integer nextSequence;
            if (appointment.getService().isSharedSeq()) {
                log.info("in shared seq");
                nextSequence = sequenceEngine.getSharedSeqToken(branchKey, appointment.getService().getSharedSequence());
            } else {
                log.info("in normal seq");
                nextSequence = sequenceEngine.getToken(branchKey, appointment.getService());
            }
            token = new Token(appointment.getService(), null, nextSequence, Type.DISPENSER, "en", appointment.getMobile(), medium, appointment.getBranch());
        }

        log.info("token: {}", token);
        token = token.appointment(appointment);
        token = tokenRepository.save(token);
        AppointmentToken appointmentToken = AppointmentToken.of(token);
        appointmentToken = appointmentTokenRepository.save(appointmentToken);
        log.info("Token : {} created for Appointment : {}", token, appointment);

        Assistance assistance = new Assistance(token);
        assistance.setMobile(mobile);
        assistance.updateDtls(appointment.getFirstName(), appointment.getEmail(), true, appointment.getAccountId(), appointment.getAccountName(), appointment.getSrnos(), null);
        Assistance assistance1 = assistanceRepository.save(assistance);
        log.info("{} created for {}", assistance1, token);

        appointment.setAssistanceid(assistance1.getId());
        appointment.setToken(token.getId());
        appointment = appointmentRepository.save(appointment);

        if (!TenantContext.getCurrentTenant().equals("queberry"))
            addMasterAppointment(appointment);
        return token;
    }

    @Override
    public Token checkin(String mobile, String checkinCode) {
        Appointment appointment = appointmentRepository.findByMobileAndCheckinCodeAndState(mobile, checkinCode, Appointment.State.CONFIRMED);
        return checkin(appointment, Token.Medium.PAPER);
    }

    @Override
    public Token checkin(String mobile, String checkinCode, Token.Medium medium) {
        log.info("in checkin with mobile");
        Optional<Appointment> oappointment = appointmentRepository.findByCheckinCodeAndDate(checkinCode, LocalDate.now());
        if (oappointment.isPresent()) {
            Appointment appointment = oappointment.get();

            if (appointment.getState().equals(Appointment.State.CANCELLED))
                throw new QueueException("Your input is incorrect. Appointment is already cancelled", HttpStatus.PRECONDITION_FAILED);

            if (appointment.getDate().isAfter(LocalDate.now()))
                throw new QueueException("Your input is incorrect. Appointment is not for today. Please checkin on the day of appointment", HttpStatus.PRECONDITION_FAILED);

            if (appointment.getDate().isBefore(LocalDate.now()))
                throw new QueueException("Your input is incorrect. Appointment is already expired.", HttpStatus.PRECONDITION_FAILED);

            if (appointment.getState().equals(Appointment.State.CHECKEDIN))
                throw new QueueException("Your input is incorrect. Appointment is already Checked-In", HttpStatus.PRECONDITION_FAILED);

            if (appointment.getState().equals(Appointment.State.COMPLETED))
                throw new QueueException("Your input is incorrect. Appointment is already Cancelled", HttpStatus.PRECONDITION_FAILED);

            if (appointment.getState().equals(Appointment.State.EXPIRED))
                throw new QueueException("Your input is incorrect. Appointment is already Expired", HttpStatus.PRECONDITION_FAILED);

            if (appointment.getState().equals(Appointment.State.NOSHOW))
                throw new QueueException("Your input is incorrect. Appointment is already Completed.", HttpStatus.PRECONDITION_FAILED);

            if (!appointment.getState().equals(Appointment.State.CONFIRMED))
                throw new QueueException("Your input is incorrect. Please verify your details and try again.", HttpStatus.PRECONDITION_FAILED);

            Token token = checkin(mobile, appointment, medium);
            return token;
        } else {
            throw new QueueException("Appointment doesn't exist!!", HttpStatus.PRECONDITION_FAILED);
        }
    }

    @Override
    public Token checkin(String checkinCode, Token.Medium medium) {
        log.info("in checkin");
        Optional<Appointment> oappointment = appointmentRepository.findByCheckinCodeAndDate(checkinCode, LocalDate.now());
        if (oappointment.isPresent()) {
            Appointment appointment = oappointment.get();

            if (appointment.getState().equals(Appointment.State.CANCELLED))
                throw new QueueException("Your input is incorrect. Appointment is already cancelled", HttpStatus.PRECONDITION_FAILED);

            if (appointment.getDate().isAfter(LocalDate.now()))
                throw new QueueException("Your input is incorrect. Appointment is not for today. Please checkin on the day of appointment", HttpStatus.PRECONDITION_FAILED);

            if (appointment.getDate().isBefore(LocalDate.now()))
                throw new QueueException("Your input is incorrect. Appointment is already expired.", HttpStatus.PRECONDITION_FAILED);

            if (appointment.getState().equals(Appointment.State.CHECKEDIN))
                throw new QueueException("Your input is incorrect. Appointment is already Checked-In", HttpStatus.PRECONDITION_FAILED);

            if (appointment.getState().equals(Appointment.State.COMPLETED))
                throw new QueueException("Your input is incorrect. Appointment is already Cancelled", HttpStatus.PRECONDITION_FAILED);

            if (appointment.getState().equals(Appointment.State.EXPIRED))
                throw new QueueException("Your input is incorrect. Appointment is already Expired", HttpStatus.PRECONDITION_FAILED);

            if (appointment.getState().equals(Appointment.State.NOSHOW))
                throw new QueueException("Your input is incorrect. Appointment is already Completed.", HttpStatus.PRECONDITION_FAILED);

            if (!appointment.getState().equals(Appointment.State.CONFIRMED))
                throw new QueueException("Your input is incorrect. Please verify your details and try again.", HttpStatus.PRECONDITION_FAILED);

            AppointmentConfiguration acr = appointmentConfigurationRepository.findAll().get(0);
            log.info("check-in time:{}", acr.getCheckInTime());

            LocalDateTime lt1 = LocalDateTime.now();
            log.info("lt1: {}", lt1);

            LocalDateTime lt2 = LocalDateTime.now();
            lt2 = lt2.withHour(appointment.getFrom().getHour());
            lt2 = lt2.withMinute(appointment.getFrom().getMinute());
            lt2 = lt2.withSecond(appointment.getFrom().getSecond());

            log.info("lt2: {}", lt2);

            lt2 = lt2.minusMinutes(acr.getCheckInTime());
            log.info("lt2 minus check: {}", lt2);

            LocalDateTime lt3 = LocalDateTime.now();
            lt3 = lt3.withHour(appointment.getFrom().getHour());
            lt3 = lt3.withMinute(appointment.getFrom().getMinute());
            lt3 = lt3.withSecond(appointment.getFrom().getSecond());

            lt3 = lt3.plusMinutes(acr.getCheckInMaxTime());
            log.info("lt3 minus check: {}", lt3);

            if (lt1.isBefore(lt2)) {
                throw new QueueException("Prior check-in of only " + acr.getCheckInTime() + "min is allowed", HttpStatus.PRECONDITION_FAILED);
            }

            if (acr.getCheckInMaxTime() > 0) {
                if (lt1.isAfter(lt3))
                    throw new QueueException("Your appointment is cancelled as checkin has not done on time", HttpStatus.PRECONDITION_FAILED);
            }
            Token token = checkin(appointment, medium);
            return token;
        } else
            throw new QueueException("Appointment doesn't exist!!", HttpStatus.PRECONDITION_FAILED);
    }

    @Override
    public Appointment checkin(String checkinCode) {
        log.info("in validate checkin");
        Optional<Appointment> oappointment = appointmentRepository.findByCheckinCodeAndDate(checkinCode, LocalDate.now());
        if (oappointment.isPresent()) {
            Appointment appointment = oappointment.get();
            log.info("Appt details: {}", appointment);
            log.info("local date: {}", LocalDate.now());

            if (appointment.getState().equals(Appointment.State.CANCELLED))
                throw new QueueException("Your input is incorrect. Appointment is already cancelled", HttpStatus.PRECONDITION_FAILED);

            if (appointment.getDate().isAfter(LocalDate.now()))
                throw new QueueException("Your input is incorrect. Appointment is not for today. Please checkin on the day of appointment", HttpStatus.PRECONDITION_FAILED);

            if (appointment.getDate().isBefore(LocalDate.now()))
                throw new QueueException("Your input is incorrect. Appointment is already expired.", HttpStatus.PRECONDITION_FAILED);

            if (appointment.getState().equals(Appointment.State.CHECKEDIN))
                throw new QueueException("Your input is incorrect. Appointment is already Checked-In", HttpStatus.PRECONDITION_FAILED);

            if (appointment.getState().equals(Appointment.State.COMPLETED))
                throw new QueueException("Your input is incorrect. Appointment is already Cancelled", HttpStatus.PRECONDITION_FAILED);

            if (appointment.getState().equals(Appointment.State.EXPIRED))
                throw new QueueException("Your input is incorrect. Appointment is already Expired", HttpStatus.PRECONDITION_FAILED);

            if (appointment.getState().equals(Appointment.State.NOSHOW))
                throw new QueueException("Your input is incorrect. Appointment is already Completed.", HttpStatus.PRECONDITION_FAILED);

            if (!appointment.getState().equals(Appointment.State.CONFIRMED))
                throw new QueueException("Your input is incorrect. Please verify your details and try again.", HttpStatus.PRECONDITION_FAILED);

            AppointmentConfiguration acr = appointmentConfigurationRepository.findAll().get(0);
            log.info("check-in time:{}", acr.getCheckInTime());

            LocalDateTime lt1 = LocalDateTime.now();
            log.info("lt1: {}", lt1);

            LocalDateTime lt2 = LocalDateTime.now();
            lt2 = lt2.withHour(appointment.getFrom().getHour());
            lt2 = lt2.withMinute(appointment.getFrom().getMinute());
            lt2 = lt2.withSecond(appointment.getFrom().getSecond());

            log.info("lt2: {}", lt2);

            lt2 = lt2.minusMinutes(acr.getCheckInTime());
            log.info("lt2 minus check: {}", lt2);

            LocalDateTime lt3 = LocalDateTime.now();
            lt3 = lt3.withHour(appointment.getFrom().getHour());
            lt3 = lt3.withMinute(appointment.getFrom().getMinute());
            lt3 = lt3.withSecond(appointment.getFrom().getSecond());

            log.info("check-in max time: {}", acr.getCheckInMaxTime());

            lt3 = lt3.plusMinutes(acr.getCheckInMaxTime());
            log.info("lt3 minus check: {}", lt3);

            if (lt1.isBefore(lt2)) {
                throw new QueueException("Check-in unsuccessful. Your appointment has not yet started. Please check in no earlier than " + acr.getCheckInTime() + " minutes before your scheduled time", HttpStatus.PRECONDITION_FAILED);
            }

            if (acr.getCheckInMaxTime() > 0) {
                if (lt1.isAfter(lt3))
                    throw new QueueException("Check-in unsuccessful. Your appointment time has passed and has been automatically cancelled. Please book a new appointment via the DMCC Member Portal", HttpStatus.PRECONDITION_FAILED);
            }
            return appointment;
        } else
            throw new QueueException("Your input is incorrect. Please verify your details and try again.", HttpStatus.PRECONDITION_FAILED);
    }

    @Override
    public void confirmCheckin(Appointment appointment) {
        log.info("in confirm checkin");
        appointment = appointmentRepository.findById(appointment.getId()).get();
        Token token = checkin(appointment, Token.Medium.SMS);
        //rabbitTemplate.convertAndSend("Checkin",new CheckinResult(token,appointment));
        log.info("Sending Token {} for Appointment : {}", token, appointment);
    }

    //Added to generate virtual token for WALKIN request -- AlMaryah Requirement
    @Override
    public void walkin(Appointment appointment) {
        appointment = appointmentRepository.findById(appointment.getId()).get();
        Token token = checkin(appointment, Token.Medium.SMS);
        log.info("Sending Token {} for walkin Appointment : {}", token, appointment);
    }

    @Override
    public Token checkin(Appointment appointment) {
//        appointment = appointmentRepository.findById(appointment.getId()).get();

        Token token = checkin(appointment, Token.Medium.SMS);
        log.info("Sending Token for Appointment : {}", new CheckinResult(token, appointment));
        return token;
    }


    @Override
    public Appointment complete(Appointment appointment) {
        log.info("in appt service complete ");
        appointment = appointmentRepository.findById(appointment.getId()).get();
        appointment.complete();
//        appointment.setDate(appointment.getAppointmentdt().toLocalDate());
        appointmentRepository.save(appointment);
        addMasterAppointment(appointment);
        return appointment;
    }

    @Override
    public Appointment noshow(Appointment appointment) {
        log.info("in appt service noshow");
        appointment = appointmentRepository.findById(appointment.getId()).get();
        appointment.noshow();
//        appointment.setDate(appointment.getAppointmentdt().toLocalDate());
        appointmentRepository.save(appointment);
        addMasterAppointment(appointment);
        return appointment;
    }

    public void addMasterAppointment(Appointment appointment) {

        String tenant = TenantContext.getCurrentTenant();
        log.info("in appt master add");
        log.info("appt id: {}", appointment.getId());
        MAppointmentList mAppointment = mAppointmentListRepository.findById(appointment.getId()).get();//.orElse(new MAppointmentList());
        mAppointment.setState(appointment.getState().toString());
        mAppointment.setCheckedInAt(appointment.getCheckedInAt());

        log.info("master rec: {}", mAppointment);
        mAppointmentListRepository.save(mAppointment);
        TenantContext.setCurrentTenant("queberry");
        mAppointmentListRepository.save(mAppointment);
        log.info("master rec saved@@");
        TenantContext.setCurrentTenant(tenant);
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckinResult implements Serializable {
        private Token token;
        private Appointment appointment;
    }
}

