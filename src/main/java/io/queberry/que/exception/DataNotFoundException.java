package io.queberry.que.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DataNotFoundException extends RuntimeException{

    private String message;

    public DataNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    @JsonIgnore
    public StackTraceElement[] getStackTrace(){
        return super.getStackTrace();
    }
}
