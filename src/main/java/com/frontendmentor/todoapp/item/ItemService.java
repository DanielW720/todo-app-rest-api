package com.frontendmentor.todoapp.item;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository repository;

    ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    List<Item> all(String uid) {
        return repository.findByUid(uid);
    }

    Item one(String id, String uid) throws UnauthorizedUserException, ItemNotFoundException {
        Item item = repository
                .findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));

        if (item.getUid().equals(uid)) {
            return item;
        } else {
            throw new UnauthorizedUserException(
                    String.format("User %s is does not have access to item %s", id, uid)
            );
        }
    }

    /**
     * Saves new item to MongoDB database unless item is not valid.
     *
     * @param item Item to save
     * @param uid  User ID
     * @return The new item
     */
    Item newItem(Item item, String uid) {
        item.setUid(uid);
        if (!itemIsValid(item))
            throw new ItemNotValidException();
        return repository.save(item);
    }

    /**
     * Checks if item is valid. An item is valid if it has a title and a user ID.
     *
     * @param item An item, may or may not be valid
     * @return True if item is valid, else false
     */
    private boolean itemIsValid(Item item) {
        return stringIsNotNullNorBlank(item.getTitle()) &&
                stringIsNotNullNorBlank(item.getUid());
    }

    /**
     * Checks if a given string is not null and not blank.
     *
     * @param s A string
     * @return True if s is neither null nor blank
     */
    private boolean stringIsNotNullNorBlank(String s) {
        return s != null && !s.isBlank();
    }

    Item replaceItem(Item updatedItem, String id) {
        return repository.findById(id)
                .map(item -> {
                    item.setUid(updatedItem.getUid());
                    item.setIsActive(updatedItem.getIsActive());
                    item.setTitle(updatedItem.getTitle());
                    item.setIndex(updatedItem.getIndex());
                    return repository.save(item);
                })
                .orElseGet(() -> {
                    updatedItem.setId(id);
                    return repository.save(updatedItem);
                });
    }

    boolean deleteItem(String id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
            return true;
        } else
            return false;
    }

}
