package io.queberry.que.role;

import io.queberry.que.subTransaction.SubTransaction;
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
public class SubServiceReport {
    private Set<SubServiceLineItem> reports = new HashSet<>(0);

    public Collection<SubServiceLineItem> getReports() {
        return reports.stream()
                .sorted(Comparator.comparing(SubServiceLineItem::subServiceName))
                .collect(Collectors.toList());
    }

    public void add(SubServiceLineItem subServiceLineItem){reports.add(subServiceLineItem);}

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubServiceLineItem{
        private SubTransaction subService;
        private Report report;
        public String subServiceName(){return subService.getName();}
    }
}
