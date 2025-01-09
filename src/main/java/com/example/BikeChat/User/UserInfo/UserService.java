package com.example.BikeChat.User.UserInfo;

import com.example.BikeChat.Firebase.FirebaseUserService;
import com.example.BikeChat.SimpleClasses.Enums.Discoverability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private FirebaseUserService firebaseUserService;

    @Autowired
    public void setFirebaseUserService(FirebaseUserService firebaseUserService) {
        this.firebaseUserService = firebaseUserService;
    }


    public void saveUser(User user) {
        firebaseUserService.saveUser(user);
    }

    public User getUser(String id) {
        return firebaseUserService.getUserByID(id);
    }

    public User getUserByUsername(String username) {
        return firebaseUserService.getUserByUsername(username);
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


    private User verifyUsername(String username) {
        try {
            User user = firebaseUserService.getUserByUsername(username);
            if (user == null) {
                System.err.println("User not found for username: " + username);
            }
            if (user == null) {
                System.err.println("DEBUG: Firebase query returned null for username: " + username);
            }
            return user;
        } catch (Exception e) {
            System.err.println("Error verifying username: " + username + ". Error: " + e.getMessage());
            return null;
        }
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
        User sender = verifyUsername(senderUsername);
        if (sender == null) {
            throw new Exception("Sender not found: " + senderUsername);
        }

        User receiver = verifyUsername(receiverUsername);
        if (receiver == null) {
            throw new Exception("Receiver not found: " + receiverUsername);
        }

        if (receiver.getPendingRequests() == null) {
            receiver.setPendingRequests(new ArrayList<>());
        }

        receiver.getPendingRequests().add(senderUsername);
        firebaseUserService.saveUser(receiver);
    }


    public void acceptFriendRequest(String senderUsername, String receiverUsername) throws Exception {
        User sender = verifyUsername(senderUsername);
        User receiver = verifyUsername(receiverUsername);

        if (sender.getFriends() == null) {
            sender.setFriends(new ArrayList<>());
        }
        if (receiver.getFriends() == null) {
            receiver.setFriends(new ArrayList<>());
        }

        sender.getFriends().add(receiverUsername);
        receiver.getFriends().add(senderUsername);

        receiver.getPendingRequests().remove(senderUsername);
        firebaseUserService.saveUser(sender);
        firebaseUserService.saveUser(receiver);
    }


    public void deleteFriend(String friendWhoDeletes, String friendWhoIsDeleted) throws Exception{
        firebaseUserService.removeFriend(friendWhoDeletes, friendWhoIsDeleted);
    }

    public List<String> returnFriendsList(String username) throws Exception {
        User user = verifyUsername(username);
        if (user != null) {
            return user.getFriends() != null ? user.getFriends() : new ArrayList<>();
        } else {
            throw new Exception("User not found");
        }
    }



    public List<String> getPendingRequests(String username) throws Exception {
        User user = verifyUsername(username); // Verify user exists
        if (user != null) {
            return user.getPendingRequests(); // Return the list of pending friend requests
        } else {
            throw new Exception("User not found");
        }
    }

    public void deleteUser(String userID) {
        firebaseUserService.deleteUser(userID);
    }

    public void updateLocationDiscoverability(String username, Discoverability discoverability){
        firebaseUserService.updateLocationDiscoverability(username, discoverability);
    }

}
