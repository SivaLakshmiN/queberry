package io.queberry.que.exception;
import org.springframework.security.core.AuthenticationException;

public class UserNotActiveException extends AuthenticationException {
    public UserNotActiveException(String msg) {
        super(msg);
    }
}
