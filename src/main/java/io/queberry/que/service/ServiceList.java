package io.queberry.que.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceList {
    Set<String> first;
    Set<String> second;
    Set<String> third;
    Set<String> fourth;

}
