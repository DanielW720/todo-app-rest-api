package com.frontendmentor.todoapp.item;

public class UnauthorizedUserException extends RuntimeException {
    UnauthorizedUserException(String message) {
        super(message);
    }
}
