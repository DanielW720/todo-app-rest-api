package com.frontendmentor.todoapp.item;

public class ItemNotValidException extends RuntimeException {

    ItemNotValidException() {
        super("Item is missing necessary properties");
    }

}
