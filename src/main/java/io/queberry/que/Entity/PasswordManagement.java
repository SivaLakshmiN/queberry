package io.queberry.que.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity(name = "que_employee_password")
@Table(name = "que_employee_password")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordManagement extends AggregateRoot<PasswordManagement> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(length = 5000)
    private String passwords;

    @Column(columnDefinition = "varchar(10)")
    private String smsOtp;

    @Column(columnDefinition = "varchar(10)")
    private String emailOtp;

    @Column(columnDefinition = "bit default 0")
    private boolean forcePasswordChange;

    @Column(columnDefinition = "varchar(2000)",unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private ResetMode resetMode;

    public void updatePasswordHistory(String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(newPassword);
        String currentPasswords = this.passwords;
        String updatedPasswords = hashedPassword + "," + currentPasswords;
        String[] passwordHistory = updatedPasswords.split(",");
        if (passwordHistory.length > 3) {
            updatedPasswords = String.join(",", passwordHistory[0], passwordHistory[1], passwordHistory[2]);
        }
        this.passwords = updatedPasswords;
    }
//public void updatePasswordHistory(String newPassword) {
//    List<String> passwordList = new ArrayList<>(Arrays.asList(passwords.split(",")));
//    if (passwordList.size() >= 5) {
//        passwordList.remove(0);
//    }
//    passwordList.add(new BCryptPasswordEncoder().encode(newPassword));
//    this.passwords = String.join(",", passwordList);
//}

}
