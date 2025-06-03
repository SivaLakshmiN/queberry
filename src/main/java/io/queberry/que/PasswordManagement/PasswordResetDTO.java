package io.queberry.que.PasswordManagement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetDTO {
    private String username;
    private String oldPassword;
    private String newPassword;
}
