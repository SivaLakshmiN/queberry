package io.queberry.que.entity;

import io.queberry.que.appointment.Appointment;

public interface AppointmentEvent {
    Appointment getAppointment();
}
