package com.softserve.edu.exception;

public class ManyMarathonsException extends RuntimeException {
    public ManyMarathonsException() {
    }

    public ManyMarathonsException(String message) {
        super(message);
    }
}
