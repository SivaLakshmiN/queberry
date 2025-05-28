package io.queberry.que.dto;

import io.queberry.que.entity.Region;
import io.queberry.que.entity.SharedSequence;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResource{
    private String id;
    private String name;
    private String displayName;
    private String description;
    private String longDescription;
    private String code;
    private boolean active;
    private Integer sequenceStart;
    private Integer sequenceEnd;
    private String seperator;
    private Integer numberOfTickets;
//    private Set<SubServiceResource> subServices;
    private Map<String,String> names = new HashMap<>(0);
    private String message;
    private Integer serveSla;
    private Integer waitSla;
    private Region region;
    private boolean sharedSeq;
    private boolean format;
    private Set<String> subServiceGroup;
    private SharedSequence sharedSequence;
    private Integer apptSlotDuration;
    private Integer priorCancelHrs;
    private Integer priorBookingDays;
    private Boolean virtualService;
}
