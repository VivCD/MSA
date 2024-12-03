package com.example.BikeChat.User;

import com.example.BikeChat.Firebase.FirebaseUserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final FirebaseUserService firebaseUserService;

    public UserService(FirebaseUserService firebaseUserService) {
        this.firebaseUserService = firebaseUserService;
    }

    public void saveUser(User user) throws Exception {
        firebaseUserService.saveUser(user);
    }

    public User getUser(String id) {
        return firebaseUserService.getUserByID(id);
    }

    public List<User> getAllUsers(){
        return firebaseUserService.getAllUsers();
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

    public void sendFriendRequest(String senderUsername, String receiverUsername) throws Exception {
        firebaseUserService.sendFriendRequest(senderUsername, receiverUsername);
    }

    public void acceptFriendRequest(String senderUsername, String receiverUsername) throws Exception {
        firebaseUserService.acceptFriendRequest(senderUsername, receiverUsername);
    }

    public void deleteFriend(String friendWhoDeletes, String friendWhoIsDeleted) throws Exception{
        firebaseUserService.removeFriend(friendWhoDeletes, friendWhoIsDeleted);
    }

    public List<String> returnFriendsList(String username) throws Exception{
        return firebaseUserService.returnFriendsList(username);
    }

}
