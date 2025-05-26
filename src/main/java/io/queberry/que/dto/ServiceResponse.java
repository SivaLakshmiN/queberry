package io.queberry.que.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {
    private String id;
    private String name;
    private String description;
    private String message;
    private int numberOfTickets;
    private int priorBookingDays;
    private int priorCancelHrs;
}
