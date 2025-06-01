package com.wazzups.analytics.userapi.Exception;

public class InvalidJsonException extends RuntimeException {
    public InvalidJsonException(String message, Throwable cause) {
        super(message, cause);
    }
}
