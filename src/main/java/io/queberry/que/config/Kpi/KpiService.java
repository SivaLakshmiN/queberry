package io.queberry.que.config.Kpi;

import io.queberry.que.appointment.Appointment;
import io.queberry.que.branch.Branch;

public interface KpiService {
    void check(String empEmail, String message);
//    void check();
//    void sendAEmail(Appointment email, String message, String emailcontent, Branch b) throws Exception;
//    void sendOtpEmail(String emailId, String otp) throws Exception;
}
