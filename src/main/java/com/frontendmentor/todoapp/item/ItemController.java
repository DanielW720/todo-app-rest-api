package com.frontendmentor.todoapp.item;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class ItemController {

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

    @GetMapping("/items/{id}")
    Item one(@RequestParam String id) {
        return service.one(id);
    }


    @PostMapping("/items")
    Item newItem(@RequestBody Item item) {
        return service.newItem(item);
    }

    @PutMapping("/items/{id}")
    Item replaceItem(@RequestBody Item newItem, @PathVariable String id) {
        return service.replaceItem(newItem, id);
    }

    @DeleteMapping("/items/{id}")
    ResponseEntity<?> deleteItem(@PathVariable String id) {
        return service.deleteItem(id) ?
                ResponseEntity.ok().body("Deleted item " + id) :
                ResponseEntity.notFound().build();
    }

}
