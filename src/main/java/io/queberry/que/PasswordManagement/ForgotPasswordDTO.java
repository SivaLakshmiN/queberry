package io.queberry.que.PasswordManagement;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordDTO {
    private String username;
    private String newPassword;
}
