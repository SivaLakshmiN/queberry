package io.queberry.que.role;

import io.queberry.que.service.Service;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
public class ServiceReport {
    private Set<ServiceLineItem> reports = new HashSet<>(0);

    public Collection<ServiceLineItem> getReports() {
        return reports.stream()
                .sorted(Comparator.comparing(ServiceLineItem::serviceName))
                .collect(Collectors.toList());
    }

    public void add(ServiceLineItem serviceLineItem){
        reports.add(serviceLineItem);}


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ServiceLineItem{
        private String service;
        private Report report;
        public String serviceName(){return service;}
        public int servingCtrsCnt=0;
    }
}
