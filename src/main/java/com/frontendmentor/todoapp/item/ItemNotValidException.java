package com.frontendmentor.todoapp.item;

public class ItemNotValidException extends RuntimeException {

    ItemNotValidException(String id) {
        super("Item " + id + " is missing necessary properties");
    }

}
