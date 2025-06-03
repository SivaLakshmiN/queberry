package io.queberry.que.config.Kpi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class KpiEngine {
//    private final KpiService kpiService;
//
//    @Scheduled(fixedRate = 300000)
//    public void check(){
//        kpiService.check();
//    }
//
//    @Async
//    @TransactionalEventListener(fallbackExecution = true)
//    public void onAppointmentCreated(Appointment.AppointmentCreated appointmentCreated) throws Exception {
//        log.info("appointment created");
//        Appointment appointment = appointmentCreated.getAppointment();
//        AppointmentConfiguration appointmentConfiguration = appointmentCreated.getAppointmentConfiguration();
//        Branch branch = appointmentCreated.getBranch();
//        if (appointmentConfiguration.isEmailAlert() && appointment.getTypeofAppointment().equals(Appointment.ModeofAppointment.F2F) && appointmentConfiguration.getConfirmedContent() != null)
//            kpiService.sendAEmail(appointment, appointmentConfiguration.getConfirmedSubject(), appointmentConfiguration.getConfirmedContent(),branch);
//
//        if (appointmentConfiguration.isEmailAlert() && appointment.getTypeofAppointment().equals(Appointment.ModeofAppointment.VIRTUAL) && appointmentConfiguration.getConfirmedVContent() != null)
//            kpiService.sendAEmail(appointment, appointmentConfiguration.getConfirmedVSubject(), appointmentConfiguration.getConfirmedVContent(),branch);
//
//    }
//
//    @Async
//    @TransactionalEventListener(fallbackExecution = true)
//    public void onAppointmentEdited(Appointment.AppointmentEdited appointmentEdited) throws Exception {
//        log.info("edited appointment");
//        Appointment appointment = appointmentEdited.getAppointment();
//        AppointmentConfiguration appointmentConfiguration = appointmentEdited.getAppointmentConfiguration();
//
//        if (appointmentConfiguration.isEmailAlert() && appointment.getTypeofAppointment().equals(Appointment.ModeofAppointment.F2F) && appointmentConfiguration.getEditedContent() != null)
//            kpiService.sendAEmail(appointment, appointmentConfiguration.getEditedSubject(), appointmentConfiguration.getEditedContent(),null);
//
//        if (appointmentConfiguration.isEmailAlert() && appointment.getTypeofAppointment().equals(Appointment.ModeofAppointment.VIRTUAL) && appointmentConfiguration.getEditedVContent() != null)
//            kpiService.sendAEmail(appointment, appointmentConfiguration.getEditedVSubject(), appointmentConfiguration.getEditedVContent(),null);
//    }
//
//    @Async
//    @TransactionalEventListener(fallbackExecution = true)
//    public void onAppointmentCancelled(Appointment.AppointmentCancelled appointmentCancelled) throws Exception {
//        log.info("appointment cancelled");
//        Appointment appointment = appointmentCancelled.getAppointment();
//        AppointmentConfiguration appointmentConfiguration = appointmentCancelled.getAppointmentConfiguration();
//
//        if (appointmentConfiguration.isEmailAlert() && appointment.getTypeofAppointment().equals(Appointment.ModeofAppointment.F2F) && appointmentConfiguration.getCancelledContent() != null)
//            kpiService.sendAEmail(appointment, appointmentConfiguration.getCancelledSubject(), appointmentConfiguration.getCancelledContent(),null);
//
//        if (appointmentConfiguration.isEmailAlert() && appointment.getTypeofAppointment().equals(Appointment.ModeofAppointment.VIRTUAL) && appointmentConfiguration.getCancelledVContent() != null)
//            kpiService.sendAEmail(appointment, appointmentConfiguration.getCancelledVSubject(), appointmentConfiguration.getCancelledVContent(),null);
//    }
//
//    public void sendOtp(String email, String otp) throws Exception {
//        log.info("Sending OTP to: {} with code: {}", email, otp);
//        kpiService.sendOtpEmail(email, otp);
//    }

}
