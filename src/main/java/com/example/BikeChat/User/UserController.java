package com.example.BikeChat.User;

import com.example.BikeChat.APIResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    // Registration endpoint
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
                return ResponseEntity.status(400).body(new ApiResponse("Username is not available.", false));
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

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            // Validate required fields
            if (loginRequest.getUsername() == null || loginRequest.getUsername().isEmpty() ||
                    loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
                return ResponseEntity.status(400).body(new ApiResponse("Missing required fields (username, password).", false));
            }

            // Authenticate the user
            boolean isAuthenticated = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());

            if (isAuthenticated) {
                return ResponseEntity.ok(new ApiResponse("User logged in successfully.", true));
            } else {
                return ResponseEntity.status(401).body(new ApiResponse("Invalid username or password.", false));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("Login failed: " + e.getMessage(), false));
        }
    }


    //test 5
    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        try {
            User user = userService.getUser(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    // Get all users
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @PostMapping("/{username}/sendRequest")
    public ResponseEntity<ApiResponse> sendFriendRequest(@PathVariable String username, @RequestParam String friendUsername) {
        try {
            userService.sendFriendRequest(username, friendUsername);
            return ResponseEntity.ok(new ApiResponse("Friend request sent.", true));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("Error: " + e.getMessage(), false));
        }
    }

    @PostMapping("/{whoAccepts}/acceptRequest")
    public ResponseEntity<ApiResponse> acceptFriendRequest(@RequestParam String usernameWhoSends, @PathVariable String whoAccepts) {
        try {
            userService.acceptFriendRequest(usernameWhoSends, whoAccepts);
            return ResponseEntity.ok(new ApiResponse("Friend request accepted.", true));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("Error: " + e.getMessage(), false));
        }
    }

    @PostMapping("/{friendWhoDeletes}/deleteFriend")
    public ResponseEntity<ApiResponse> deleteFriend(@PathVariable String friendWhoDeletes, @RequestParam String friendToBeDeleted){
        try{
            userService.deleteFriend(friendWhoDeletes, friendToBeDeleted);
            return ResponseEntity.ok(new ApiResponse("Friend deleted.", true));
        } catch (Exception e){
            return ResponseEntity.status(500).body(new ApiResponse("Error: " + e.getMessage(), false));
        }
    }

    @GetMapping("/getFriendsList")
    public ResponseEntity<List<String>> getFriendsList(@RequestParam String usernameWhoRequestsList) {
        try {
            List<String> friends = userService.returnFriendsList(usernameWhoRequestsList);
            return ResponseEntity.ok(friends);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
