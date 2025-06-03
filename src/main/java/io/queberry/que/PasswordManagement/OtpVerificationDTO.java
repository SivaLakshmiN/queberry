package io.queberry.que.PasswordManagement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpVerificationDTO {

    private String username;
    private String otp;
    private String type;
}
