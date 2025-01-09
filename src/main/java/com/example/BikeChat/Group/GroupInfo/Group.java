package com.example.BikeChat.Group.GroupInfo;


import com.google.api.client.util.DateTime;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.j2objc.annotations.Property;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


import java.util.List;

@Entity
public class Group {
    @DocumentId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String groupID;

    @Property String groupName;
    @Property
    private String creatorUsername;
    @Property
    private List<String> participantsUsernames;
    @Property
    private boolean active;


    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public List<String> getParticipantsUsernames() {
        return participantsUsernames;
    }

    public void setParticipantsUsernames(List<String> participantsUsernames) {
        this.participantsUsernames = participantsUsernames;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


}
