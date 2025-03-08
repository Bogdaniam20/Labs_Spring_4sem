package com.springlabs.exceptions;

public class InfoNotFoundException extends RuntimeException {
    public InfoNotFoundException(String message) {
        super(message);
    }
}
