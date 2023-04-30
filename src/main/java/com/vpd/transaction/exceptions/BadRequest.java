package com.vpd.transaction.exceptions;

public class BadRequest extends RuntimeException {
    public BadRequest(String message) {
        super(message);
    }
    public BadRequest(String message, Throwable cause) {
        super(message, cause);
    }
}
