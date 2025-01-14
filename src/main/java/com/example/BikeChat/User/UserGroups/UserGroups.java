package com.example.BikeChat.User.UserGroups;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.j2objc.annotations.Property;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.List;
import java.util.Map;

@Entity
public class UserGroups {
    @Id
    private String username;

    @ElementCollection
    private List<Map<String, String>> groups;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Map<String, String>> getGroupIds() {
        return groups;
    }

    public void setGroupIds(List<Map<String, String>> groupIds) {
        this.groups = groupIds;
    }
}
