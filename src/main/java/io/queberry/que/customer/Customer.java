package io.queberry.que.customer;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.queberry.que.entity.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Random;

@Entity(name = "customer")
@Table(name = "customer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends AggregateRoot<Customer> {

    @Column(unique = true)
    @NotNull(message = "Username is required.")
    @Pattern(regexp =  "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$", message = "Email is invalid")
    private String username; // email

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String fullName;

    @NotNull(message = "Provide mobile number!! ")
    @Pattern(regexp = "[0-9]*", message = "Mobile number should contain only numbers!! ")
    private String mobile;

    private String smsotp;

    private boolean otpVerified = false;

    private LocalDateTime otpVerifiedDt;

    public static Integer getRandom6DigitNumber(){
        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);
        return n;
    }

}

