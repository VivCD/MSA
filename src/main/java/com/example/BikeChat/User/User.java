package com.example.BikeChat.User;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.j2objc.annotations.Property;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {

    @DocumentId
    private Long userID;
    @Property
    private String username;
    @Property
    private String email;
    @Property
    private String passwordHash;
    @Property
    private String profilePictureUrl;
    @Property
    private String bio;



    public User(Long userID, String username, String email, String passwordHash, String profilePictureUrl, String bio){
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.profilePictureUrl = profilePictureUrl;
        this.bio = bio;
    }


    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
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
}
