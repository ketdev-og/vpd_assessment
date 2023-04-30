package com.vpd.transaction.exceptions;

public class InvalidUserCredentialException extends RuntimeException{
    public InvalidUserCredentialException(String message){
       super(message);
    }

    public InvalidUserCredentialException(String message, Throwable throwable){
        super(message, throwable);
    }
}
