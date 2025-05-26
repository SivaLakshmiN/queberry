package io.queberry.que.exception;

import lombok.Getter;

@Getter
public class QueException extends RuntimeException {

    String code;
    String message;

    public QueException(String code, Exception e) {
    }

    public String getCode() {
        return code;
    }

    public QueException(String code, String message) {
        super(code + " : " + message);
        this.code = code;
        this.message = message;
    }

    public QueException(String message) {
        super(message);
    }

}