package io.queberry.que.serviceGroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.queberry.que.entity.AggregateRoot;
import io.queberry.que.entity.Service;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "ServiceGroupCache")
public class ServiceGroup extends AggregateRoot<ServiceGroup> {

    @NotNull()
    @Column(unique = true,columnDefinition = "nvarchar(200)",nullable = false)
    private String name;

    @NotNull()
    @Column(columnDefinition = "nvarchar(200)",nullable = false)
    private String displayName;

    private String description;

    @Column(columnDefinition = "nvarchar(255)")
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String,String> names = new HashMap<>(0);

//    @ManyToMany(fetch = FetchType.EAGER)
//    @NotNull()
    @ElementCollection(fetch = FetchType.EAGER)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "ServiceCache")
    private Set<String> services = new HashSet<>(0);

    public ServiceGroup service(String service){
        this.services.add(service);
        return this;
    }

    public ServiceGroup services(Set<String> services){
        this.services.addAll(services);
        return this;
    }

    public ServiceGroup clearServices(){
        this.services.clear();
        return this;
    }

    public ServiceGroup deleteService(Service service){
        this.services.remove(service);
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Map<String, String> getNames() {
        return names;
    }

    public Set<String> getServices() {
        return Collections.unmodifiableSet(this.services);
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Set<String> getServicesMeta() {
        return Collections.unmodifiableSet(this.services);
    }
}

