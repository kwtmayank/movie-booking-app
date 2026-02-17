package com.example.moviebooking.exception;

public class InvalidGoogleTokenException extends RuntimeException {
    public InvalidGoogleTokenException(String message) {
        super(message);
    }

    public InvalidGoogleTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
