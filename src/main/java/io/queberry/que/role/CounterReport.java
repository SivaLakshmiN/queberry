package io.queberry.que.role;

import io.queberry.que.counter.Counter;
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
public class CounterReport {

    private Set<CounterLineItem> reports = new HashSet<>(0);

    public Collection<CounterLineItem> getReports() {
        return reports.stream()
                .sorted(Comparator.comparing(CounterLineItem::counterName)) //comparator - how you want to sort it
                .collect(Collectors.toList());
    }

    public void add(CounterLineItem counterLineItem){reports.add(counterLineItem);}

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CounterLineItem{
        private Counter counter;
        private Report report;
        public String counterName(){return counter.getName();}
    }
}
