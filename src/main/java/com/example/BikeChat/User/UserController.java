package com.example.BikeChat.User;

import com.example.BikeChat.Firebase.FirebaseService;
import org.springframework.web.bind.annotation.*;

public class UserController {
    private final FirebaseService firebaseService;

    public UserController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @PostMapping
    public void createUser(@RequestBody User user){
        firebaseService.saveUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id){
        return firebaseService.getUser(id);
    }
}
