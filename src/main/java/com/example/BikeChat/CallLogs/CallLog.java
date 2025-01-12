package com.example.BikeChat.CallLogs;

import com.google.api.client.util.DateTime;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.j2objc.annotations.Property;
import jakarta.persistence.*;

import java.sql.Time;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Entity
public class CallLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String callLogID;

    @Property
    private String groupID;
    @ElementCollection
    private List<String> participantsID;
    @Property
    private Instant startTime;
    @Property
    private  Instant endTime;
    @Property
    private long duration;
    @Property
    private boolean active;

    public String getCallLogID() {
        return callLogID;
    }

    public void setCallLogID(String callLogID) {
        this.callLogID = callLogID;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public List<String> getParticipantsID() {
        return participantsID;
    }

    public void setParticipantsID(List<String> participantsID) {
        this.participantsID = participantsID;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
