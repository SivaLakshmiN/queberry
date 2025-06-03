package io.queberry.que.Counter;

import io.queberry.que.dto.BaseResource;
import io.queberry.que.enums.Type;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CounterResource extends BaseResource {

    private String name;

    private String description;

    private String code;

    private boolean active = true;

    private Type type;

    private Set<String> firstLevelServicesMeta = new HashSet<>(0);

    private Set<String> secondLevelServicesMeta = new HashSet<>(0);

    private Set<String> thirdLevelServicesMeta = new HashSet<>(0);

    private Set<String> fourthLevelServicesMeta = new HashSet<>(0);
}

