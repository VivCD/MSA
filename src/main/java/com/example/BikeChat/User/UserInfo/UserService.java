package com.example.BikeChat.User.UserInfo;

import com.example.BikeChat.Firebase.FirebaseUserService;
import com.example.BikeChat.SimpleClasses.Enums.Discoverability;
import com.example.CustomExceptions.UserNotFoundException;
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

    public void sendFriendRequestShort(String senderUsername, String receiverUsername){
        if(verifyUsername(senderUsername) == null){
            throw new UserNotFoundException("User with username " + senderUsername + "doesn't exist");
        }
        if(verifyUsername(receiverUsername) == null){
            throw new UserNotFoundException("User with username " + receiverUsername + "doesn't exist");
        }
        firebaseUserService.sendFriendRequest(senderUsername, receiverUsername);
    }

    public void acceptFriendRequestShort(String senderUsername, String receiverUsername){
        if(verifyUsername(senderUsername) == null){
            throw new UserNotFoundException("User with username " + senderUsername + "doesn't exist");
        }
        if(verifyUsername(receiverUsername) == null){
            throw new UserNotFoundException("User with username " + receiverUsername + "doesn't exist");
        }
        firebaseUserService.acceptFriendRequest(senderUsername, receiverUsername);
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

    public void updateBio(String username, String bio){
        firebaseUserService.updateBio(username, bio);
    }
    public void updateEmail(String username, String email){
        firebaseUserService.updateEmail(username, email);
    }
    public void updateProfilePicture(String username, String profilePictureUrl){
        firebaseUserService.updateProfilePicture(username, profilePictureUrl);
    }


}
