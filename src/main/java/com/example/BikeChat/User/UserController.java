package com.example.BikeChat.User;

import com.example.BikeChat.Firebase.FirebaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final FirebaseService firebaseService;

    public UserController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user){
        try {
            firebaseService.saveUser(user);
            return ResponseEntity.ok("User successfully created and saved.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving user: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id){
        return firebaseService.getUser(id);
    }
}
