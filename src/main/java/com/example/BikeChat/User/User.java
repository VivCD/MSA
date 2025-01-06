package com.example.BikeChat.User;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.j2objc.annotations.Property;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @DocumentId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userID;
    @Property
    private String username;
    @Property
    private String email;



    @Property
    private String password;
    @Property
    private String passwordHash;
    @Property
    private String profilePictureUrl;
    @Property
    private String bio;

    @Property
    private List<String> friends = new ArrayList<>();

    @Property
    private List<String> pendingRequests = new ArrayList<>();



    public User() {
    }

    public User(String userID, String username, String email, String password, String profilePictureUrl, String bio){
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profilePictureUrl = profilePictureUrl;
        this.bio = bio;
        this.friends = new ArrayList<>();
        this.pendingRequests = new ArrayList<>();
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    // Getters and Setters
    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(List<String> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }
}
