package com.frontendmentor.todoapp.item;

public class ItemNotFoundException extends RuntimeException {

    ItemNotFoundException(String id) {
        super("Could not find item " + id);
    }

}
