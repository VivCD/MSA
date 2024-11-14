package com.example.BikeChat.User;

import com.example.BikeChat.Firebase.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {

            if(userService.isUsernameAvailable(user.getUsername())) {

                String hashedPassword = userService.encryptUserPassword(user.getPassword());
                user.setPasswordHash(hashedPassword);
                user.setPassword(null);

                userService.saveUser(user);
                return ResponseEntity.ok("User successfully created and saved.");
            }else
                return ResponseEntity.status(401).body("Username is not available");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving user: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest){
        try{
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            boolean isAuthenticated = userService.authenticateUser(username, password);

            if (isAuthenticated) {
                return ResponseEntity.ok("User logged in successfully.");
            } else {
                return ResponseEntity.status(401).body("Invalid username or password.");
            }
        }catch (Exception e) {
            return ResponseEntity.status(500).body("Login failed: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    @GetMapping("/all")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
}
