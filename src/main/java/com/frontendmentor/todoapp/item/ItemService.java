package com.frontendmentor.todoapp.item;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository repository;

    ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    List<Item> all() {
        return repository.findAll();
    }

    Item one(String id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
    }

    Item newItem(Item item) {
        if (!itemIsValid(item))
            throw new ItemNotValidException(item.getId());
        return repository.save(item);
    }

    private boolean itemIsValid(Item item) {
        return (item.getTitle() != null);
    }

    Item replaceItem(Item newItem, String id) {
        return repository.findById(id)
                .map(item -> {
                    item.setIsActive(newItem.getIsActive());
                    item.setTitle(newItem.getTitle());
                    item.setIndex(newItem.getIndex());
                    return repository.save(item);
                })
                .orElseGet(() -> {
                    newItem.setId(id);
                    return repository.save(newItem);
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
