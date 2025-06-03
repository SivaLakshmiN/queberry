package io.queberry.que.Counter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CounterResources {
    private String name;
    private String displayName;
    private String description;
    private String code;
    private String type;
    private String branchKey;
    private boolean active=true;
    Set<String> first;
    Set<String> second;
    Set<String> third;
    Set<String> fourth;
    private String comPort;
    private String colorCode;
    private String panelNumber;
    private String presentation;
}