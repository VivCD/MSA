package com.example.BikeChat.User;

import com.example.BikeChat.Firebase.FirebaseService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final FirebaseService firebaseService;

    public UserService(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    public void saveUser(User user) throws Exception {
        firebaseService.saveUser(user);
    }

    public User getUser(String id) {
        return firebaseService.getUserByID(id);
    }

    public List<User> getAllUsers(){
        return firebaseService.getAllUsers();
    }

    public String encryptUserPassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return passwordEncoder.encode(password);
    }

    public boolean isUsernameAvailable(String username){
        if(verifyUsername(username) == null)
            return true;
        return false;
    }


    private User verifyUsername(String username){
        List<User> allUsers = new ArrayList<>();
        allUsers = getAllUsers();

        for(User u : allUsers)
            if(username.equals(u.getUsername()))
                return u;
        return null;
    }
    private boolean verifyPassword(String rawPassword, String hashedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    public boolean authenticateUser(String username, String password) {
        User user = verifyUsername(username);
        if (user == null) {
            return false; // Username not found
        }
        return verifyPassword(password, user.getPasswordHash()); // Check password
    }

    public void sendFriendRequest(String userId, String friendId) throws Exception {
        firebaseService.sendFriendRequest(userId, friendId);
    }

    public void acceptFriendRequest(String userId, String friendId) throws Exception {
        firebaseService.acceptFriendRequest(userId, friendId);
    }

}
