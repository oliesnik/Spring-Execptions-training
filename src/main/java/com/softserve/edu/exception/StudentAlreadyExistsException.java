package com.softserve.edu.exception;

public class StudentAlreadyExistsException extends RuntimeException{
    public StudentAlreadyExistsException() {
    }

    public StudentAlreadyExistsException(String message) {
        super(message);
    }
}
