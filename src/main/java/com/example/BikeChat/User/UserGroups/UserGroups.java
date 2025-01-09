package com.example.BikeChat.User.UserGroups;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.j2objc.annotations.Property;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
public class UserGroups {
    @DocumentId
    private String username;
    @Property
    private List<String> groupIds;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
    }
}
