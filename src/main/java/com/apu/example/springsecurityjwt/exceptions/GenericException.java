package com.apu.example.springsecurityjwt.exceptions;

public class GenericException extends Exception {

    public GenericException(String message) {
        super(message);
    }

    public GenericException(Throwable throwable) {
        super(throwable);
    }

    public GenericException(String message, Throwable cause) {
        super(message, cause);
    }
}
