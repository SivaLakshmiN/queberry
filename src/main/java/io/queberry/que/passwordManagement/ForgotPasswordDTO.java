package io.queberry.que.passwordManagement;

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
