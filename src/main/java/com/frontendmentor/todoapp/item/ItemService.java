package com.frontendmentor.todoapp.item;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository repository;

    ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    /**
     * Fetch as items belonging to user.
     *
     * @param uid User ID
     * @return List of items belonging to user
     */
    List<Item> all(String uid) {
        return repository.findByUid(uid);
    }

    /**
     * Fetches item.
     *
     * @param id  ID of the item
     * @param uid User ID
     * @return Item
     * @throws UnauthorizedUserException User do not own the item
     * @throws ItemNotFoundException     Item with provided ID not found
     */
    Item one(String id, String uid) throws UnauthorizedUserException, ItemNotFoundException {
        Item item = repository
                .findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));

        if (item.getUid().equals(uid)) {
            return item;
        } else {
            throw new UnauthorizedUserException(
                    String.format("User %s is does not have access to item %s", uid, id)
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
        if (itemNotValid(item))
            throw new ItemNotValidException();
        return repository.save(item);
    }

    /**
     * Checks if item is valid. An item is valid if it has a title and a user ID.
     *
     * @param item An item, may or may not be valid
     * @return True if item is valid, else false
     */
    private boolean itemNotValid(Item item) {
        return stringIsNullOrBlank(item.getTitle()) ||
                stringIsNullOrBlank(item.getUid());
    }

    /**
     * Checks if a given string is not null and not blank.
     *
     * @param s A string
     * @return True if s is neither null nor blank
     */
    private boolean stringIsNullOrBlank(String s) {
        return s == null || s.isBlank();
    }

    /**
     * Updates item, but only if item belongs user and is valid.
     * If item do not belong to user, throw exception. If item is not found, create new item.
     *
     * @param updatedItem Updated item
     * @param id          ID of item to update
     * @param uid         User ID (identifying the user to whom the item belong)
     * @return Updated item
     * @throws UnauthorizedUserException User do not own the item
     * @throws ItemNotValidException     Updated item is not valid
     */
    Item replaceItem(Item updatedItem, String id, String uid) throws UnauthorizedUserException, ItemNotValidException {
        return repository.findById(id)
                .map(item -> {
                    if (!item.getUid().equals(uid)) {
                        throw new UnauthorizedUserException(
                                String.format("User %s is does not have access to item %s", uid, id)
                        );
                    }
                    item.setIsActive(updatedItem.getIsActive());
                    item.setTitle(updatedItem.getTitle());
                    item.setIndex(updatedItem.getIndex());
                    if (itemNotValid(item)) {
                        throw new UnauthorizedUserException(
                                String.format("User %s is does not have access to item %s", uid, id)
                        );
                    }
                    return repository.save(item);
                })
                .orElseGet(() -> {
                    updatedItem.setId(id);
                    updatedItem.setUid(uid);
                    if (itemNotValid(updatedItem))
                        throw new ItemNotValidException();
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
