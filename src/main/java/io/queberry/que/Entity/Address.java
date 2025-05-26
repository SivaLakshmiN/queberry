package io.queberry.que.Entity;

import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.data.geo.Point;

import static org.springframework.beans.BeanUtils.copyProperties;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class Address extends AuditedEntity {
    private String building;
    private String street;
    private String area;
    private String city;
    private String zip;
    private String country;
    private Point location;

    @Override
    protected Address clone() {
        Address target = null;
        copyProperties(this, target);

        return target;
    }
}
