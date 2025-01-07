package com.example.BikeChat.User.UserLocation;

import com.example.BikeChat.User.UserInfo.User;
import com.example.CustomExceptions.InvalidGroupDetailsException;
import com.example.CustomExceptions.InvalidUserLocationDetails;
import com.google.api.client.util.DateTime;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserLocationService {
    @Autowired
    private Firestore firestore;

    public void updateLocation(String username, double latitude, double longitude, DateTime creationDate){
        checkData(username, latitude, longitude);
        DocumentReference userLocRef = getUserLocationReference(username);
        if(userLocRef == null)
            userLocRef = createUserLocationDoc(username);

        ApiFuture<WriteResult> futureLat = userLocRef.update("latitude", latitude);
        ApiFuture<WriteResult> futureLong = userLocRef.update("longitude", longitude);
        ApiFuture<WriteResult> futureDateTime = userLocRef.update("creationDate", creationDate);

        List<ApiFuture<WriteResult>> futures = List.of(futureLat, futureLong, futureDateTime);
        try{
            List<WriteResult> results = ApiFutures.allAsList(futures).get();
            for(WriteResult r : results)
                System.out.println("Update Successfull at: " + r.getUpdateTime());

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to update the details of userLocation");
        }
    }

    private DocumentReference getUserLocationReference(String username){
        DocumentReference docRef = firestore.collection("UserLocations").document(username);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try{
            DocumentSnapshot userLocationDoc = future.get();
            if(userLocationDoc.exists())
                return docRef;
            else {
                return null;
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to get the document reference for the username");
        }
    }

    private DocumentSnapshot getUserLocationSnapshot(String username){
        DocumentReference docRef = firestore.collection("UserLocations").document(username);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try{
            DocumentSnapshot userLocationDoc = future.get();
            if(userLocationDoc.exists())
                return userLocationDoc;
            else {
                throw new InvalidUserLocationDetails("UserLocation does not exist");
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to get the document snapshot for the username");
        }
    }

    private DocumentReference createUserLocationDoc(String username){
        UserLocation userLocation = new UserLocation();
        userLocation.setUsername(username);
        DocumentReference docRef = firestore.collection("UserLocations").document(username);
        ApiFuture<WriteResult> result = docRef.set(userLocation);

        return docRef;
    }

    public void deleteUserLocation(String username){
        try{

            DocumentReference docRef = getUserLocationReference(username);
            if(docRef == null)
                throw new InvalidUserLocationDetails("UserLocation doesn't exist!");
            ApiFuture<WriteResult> future = docRef.delete();
            WriteResult result = future.get();
            System.out.println("Document deleted at: " + result.getUpdateTime());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to delete user");
        }
    }

    private void checkData(String username, double latitude, double longitude){
        if(username.isEmpty())
            throw new InvalidUserLocationDetails("Invalid username");
        if (latitude < -90 || latitude > 90) {
            throw new InvalidUserLocationDetails("Invalid latidude");
        }
        if (longitude < -180 || longitude > 180) {
            throw new InvalidUserLocationDetails("Invalid longitude");
        }
    }
}
