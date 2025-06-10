package io.queberry.que.config.Appointment;

import io.queberry.que.appointment.Appointment;
import io.queberry.que.token.Token;

public interface AppointmentService {
    Token checkin(Appointment appointment, Token.Medium medium);
    Token checkin(String mobile,Appointment appointment, Token.Medium medium);
    Token checkin(String mobile, String checkinCode);
    Token checkin(String mobile, String checkinCode, Token.Medium medium);
    Token checkin(String checkinCode, Token.Medium medium);
    Appointment checkin(String checkinCode);
    void confirmCheckin(Appointment appointment);
    void walkin(Appointment appointment);
    Appointment complete(Appointment appointment);
    Appointment noshow(Appointment appointment);
    Token checkin(Appointment appointment);
}
