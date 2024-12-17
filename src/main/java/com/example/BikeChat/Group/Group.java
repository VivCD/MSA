package com.example.BikeChat.Group;

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
    @Property
    private String hostID;
    @Property
    private List<String> participantsID;
    @Property
    private Boolean active;
    @Property
    private DateTime creationDate;

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getHostID() {
        return hostID;
    }

    public void setHostID(String hostID) {
        this.hostID = hostID;
    }

    public List<String> getParticipantsID() {
        return participantsID;
    }

    public void setParticipantsID(List<String> participantsID) {
        this.participantsID = participantsID;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }
}
