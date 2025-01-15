package com.example.BikeChat.User.UserLocation;

import com.example.CustomExceptions.InvalidGroupDetailsException;
import com.example.CustomExceptions.InvalidUserLocationDetailsException;
import com.google.api.client.util.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/location")
public class UserLocationController {
    @Autowired
    UserLocationService userLocationService;

    @PutMapping("/updateLocation")
    public ResponseEntity<String> updateLocation(
            @RequestParam String username,
            @RequestParam double latitude,
            @RequestParam double longitude) {
        try{
            userLocationService.updateLocation(username, latitude, longitude);
            return ResponseEntity.status(HttpStatus.OK).body("Location updated successfully");
        } catch ( InvalidUserLocationDetailsException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }  catch ( RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getNearbyLocations")
    public ResponseEntity<?> getNearbyLocations(String username){
        try{
            List<UserLocation> userLocations = userLocationService.getUserLocations(username);
            return ResponseEntity.ok(userLocations);
        } catch (InvalidGroupDetailsException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch ( RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteLocation")
    public ResponseEntity<String> deleteLocation(@RequestParam String username){
        try{
            userLocationService.deleteUserLocation(username);
            return ResponseEntity.status(HttpStatus.OK).body("User location deleted successfully");
        } catch ( RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }



}
