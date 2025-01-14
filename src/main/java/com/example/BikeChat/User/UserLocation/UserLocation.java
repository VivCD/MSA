package com.example.BikeChat.User.UserLocation;

import com.example.BikeChat.SimpleClasses.Enums.Discoverability;
import com.google.api.client.util.DateTime;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.j2objc.annotations.Property;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class UserLocation {
    @DocumentId
    private String username;
    @Property
    private double latitude;
    @Property
    private double longitude;



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }



}
