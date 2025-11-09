package com.fundae.backend.Exception;

public class EmailInUseException extends RuntimeException {
    public EmailInUseException(String message) { super(message); }
}