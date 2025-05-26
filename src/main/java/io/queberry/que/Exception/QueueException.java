package io.queberry.que.Exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class QueueException extends RuntimeException {

    private final HttpStatus status;

    public QueueException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    @JsonIgnore
    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    public HttpStatus getHttpStatus() {
        return this.status;
    }
}


