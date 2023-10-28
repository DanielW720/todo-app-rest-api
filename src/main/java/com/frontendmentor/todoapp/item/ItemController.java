package com.frontendmentor.todoapp.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class ItemController {

    Logger logger = LoggerFactory.getLogger(ItemController.class);
    ItemService service;

    ItemController(ItemService service) {
        this.service = service;
    }

    @GetMapping("/items")
    Map<String, List<Item>> all() {
        return Collections.singletonMap(
                "items",
                service.all()
        );
    }

    @GetMapping("/items/item")
    Item one(@RequestParam String id) {
        return service.one(id);
    }

    @PostMapping("/items")
    Item newItem(@RequestBody Item item) {
        try {
            return service.newItem(item);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }
    }

    @PutMapping("/items/{id}")
    Item replaceItem(@RequestBody Item updatedItem, @PathVariable String id) {
        return service.replaceItem(updatedItem, id);
    }

    @DeleteMapping("/items/{id}")
    ResponseEntity<?> deleteItem(@PathVariable String id) {
        return service.deleteItem(id) ?
                ResponseEntity.ok().body("Deleted item " + id) :
                ResponseEntity.notFound().build();
    }

}
