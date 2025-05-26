package io.queberry.que.entity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Role extends AggregateRoot<Role> implements GrantedAuthority {

    private String name;
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> pageRights = new HashSet<>(0);

    @Override
    public String getAuthority() {
        return name;
    }

//    public Role(String name){
//        this(name, name);
//    }


}

