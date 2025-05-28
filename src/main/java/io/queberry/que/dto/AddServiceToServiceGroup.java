package io.queberry.que.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddServiceToServiceGroup {
    private Set<String> services;
    private String name;
    private String description;
    private Map<String,String> names;
}
