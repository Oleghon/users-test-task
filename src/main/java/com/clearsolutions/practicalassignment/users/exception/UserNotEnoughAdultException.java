package com.clearsolutions.practicalassignment.users.exception;

public class UserNotEnoughAdultException extends RuntimeException {
    public UserNotEnoughAdultException(String message) {
        super(message);
    }
}
