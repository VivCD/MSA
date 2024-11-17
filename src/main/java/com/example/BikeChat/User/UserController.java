package com.example.BikeChat.User;

import com.example.BikeChat.Firebase.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import com.example.BikeChat.User.ApiResponse;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> createUser(@RequestBody User user) {
        try {
            // Validate required fields
            if (user.getUsername() == null || user.getUsername().isEmpty() ||
                    user.getEmail() == null || user.getEmail().isEmpty() ||
                    user.getPassword() == null || user.getPassword().isEmpty()) {
                return ResponseEntity.status(400).body(new ApiResponse("Missing required fields (username, email, password).", false));
            }

            // Check if username is available
            if (!userService.isUsernameAvailable(user.getUsername())) {
                return ResponseEntity.status(400).body(new ApiResponse("Username is not available", false));
            }


            // Generate a unique userID if not provided
            if (user.getUserID() == null || user.getUserID().isEmpty()) {
                user.setUserID(UUID.randomUUID().toString());
            }

            // Hash the password and clear the plain text
            String hashedPassword = userService.encryptUserPassword(user.getPassword());
            user.setPasswordHash(hashedPassword);
            user.setPassword(null);

            // Set default values for optional fields
            if (user.getBio() == null) {
                user.setBio("");
            }
            if (user.getProfilePictureUrl() == null) {
                user.setProfilePictureUrl("default-profile-picture-url"); // Replace with your default URL
            }

            // Save the user to the database
            userService.saveUser(user);

            // Return success response
            return ResponseEntity.ok(new ApiResponse("User successfully created and saved.", true));

        } catch (Exception e) {
            // Return error response on exception
            return ResponseEntity.status(500).body(new ApiResponse("Error saving user: " + e.getMessage(), false));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            boolean isAuthenticated = userService.authenticateUser(username, password);

            if (isAuthenticated) {
                return ResponseEntity.ok(new ApiResponse("User logged in successfully.", true));
            } else {
                return ResponseEntity.status(401).body(new ApiResponse("Invalid username or password.", false));
            }

            if(userService.isUsernameAvailable(user.getUsername())) {

                String hashedPassword = userService.encryptUserPassword(user.getPassword());
                user.setPasswordHash(hashedPassword);
                user.setPassword(null);

                userService.saveUser(user);
                return ResponseEntity.ok("User successfully created and saved.");
            }else
                return ResponseEntity.status(401).body("Username is not available");

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("Login failed: " + e.getMessage(), false));
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
