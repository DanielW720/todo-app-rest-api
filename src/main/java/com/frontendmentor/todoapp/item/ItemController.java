package com.frontendmentor.todoapp.item;

import com.frontendmentor.todoapp.utils.FirebaseAuthenticationUtil;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
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

    // Todo: return a ResponseEntity
    @GetMapping("/items")
    Map<String, List<Item>> all(@RequestHeader("Authorization") String idToken) {
        try {
            FirebaseToken decodedToken = FirebaseAuthenticationUtil.verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            return Collections.singletonMap("items", service.all(uid));
        } catch (FirebaseAuthException e) {
            logger.error("User verification failed. Error message: {}", e.getMessage());
            // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            return Collections.singletonMap("items", Collections.emptyList());
        }
    }

    // Todo: return a ResponseEntity
    @GetMapping("/items/item")
    Item one(@RequestParam String id, @RequestHeader("Authorization") String idToken) {
        try {
            FirebaseToken decodedToken = FirebaseAuthenticationUtil.verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            return service.one(id, uid);
        } catch (FirebaseAuthException e) {
            logger.error("User verification failed. Error message: {}", e.getMessage());
            return null;
        } catch (UnauthorizedUserException e) {
            logger.error("Access not allowed. Error message: {}", e.getMessage());
            return null;
        } catch (ItemNotFoundException e) {
            logger.error("Item {} was not found. Error message: {}", id, e.getMessage());
            return null;
        }
    }

    // Todo: return a ResponseEntity
    @PostMapping("/items")
    Item newItem(@RequestBody Item item, @RequestHeader("Authorization") String idToken) {
        try {
            FirebaseToken decodedToken = FirebaseAuthenticationUtil.verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            return service.newItem(item, uid);
        } catch (FirebaseAuthException e) {
            logger.error("User verification failed. Error message: {}", e.getMessage());
            return null;
        } catch (ItemNotValidException e) {
            logger.error("Item not valid. Error message: {}", e.getMessage());
            return null;
        }
    }

    // Todo: return a ResponseEntity
    @PutMapping("/items/{id}")
    Item replaceItem(@RequestBody Item updatedItem, @PathVariable String id, @RequestHeader("Authorization") String idToken) {
        try {
            FirebaseToken decodedToken = FirebaseAuthenticationUtil.verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            return service.replaceItem(updatedItem, id, uid);
        } catch (FirebaseAuthException e) {
            logger.error("User verification failed. Error message: {}", e.getMessage());
            return null;
        } catch (UnauthorizedUserException e) {
            logger.error("Access not allowed. Error message: {}", e.getMessage());
            return null;
        } catch (ItemNotValidException e) {
            logger.error("Item not valid. Error message: {}", e.getMessage());
            return null;
        }
    }

    @DeleteMapping("/items/{id}")
    ResponseEntity<?> deleteItem(@PathVariable String id, @RequestHeader("Authorization") String idToken) {
        try {
            FirebaseToken decodedToken = FirebaseAuthenticationUtil.verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            return service.deleteItem(id, uid) ?
                    ResponseEntity.ok().body("Deleted item " + id) :
                    ResponseEntity.notFound().build();
        } catch (FirebaseAuthException e) {
            logger.error("User verification failed. Error message: {}", e.getMessage());
            return null;
        }
    }

}
