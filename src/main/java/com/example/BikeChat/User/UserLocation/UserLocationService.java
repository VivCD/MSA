package com.example.BikeChat.User.UserLocation;

import com.example.BikeChat.Firebase.FirebaseUserService;
import com.example.BikeChat.SimpleClasses.Enums.Discoverability;
import com.example.BikeChat.User.UserInfo.User;
import com.example.CustomExceptions.InvalidUserLocationDetailsException;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.BikeChat.SimpleClasses.Enums.Discoverability.EVERYONE;

@Service
public class UserLocationService {
    @Autowired
    private Firestore firestore;
    @Autowired
    private FirebaseUserService firebaseUserService;
    private static final double EARTH_RADIUS = 6371e3;

    public void updateLocation(String username, double latitude, double longitude ){
        checkData(username, latitude, longitude);
        DocumentReference userLocRef = getUserLocationReference(username);
        if(userLocRef == null)
            userLocRef = createUserLocationDoc(username);

        ApiFuture<WriteResult> futureLat = userLocRef.update("latitude", latitude);
        ApiFuture<WriteResult> futureLong = userLocRef.update("longitude", longitude);

        List<ApiFuture<WriteResult>> futures = List.of(futureLat, futureLong);
        try{
            List<WriteResult> results = ApiFutures.allAsList(futures).get();

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
                throw new InvalidUserLocationDetailsException("UserLocation does not exist");
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
                throw new InvalidUserLocationDetailsException("UserLocation doesn't exist!");
            ApiFuture<WriteResult> future = docRef.delete();
            WriteResult result = future.get();
            System.out.println("Document deleted at: " + result.getUpdateTime());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to delete user");
        }
    }

    public List<UserLocation> getUserLocations(String username){
        UserLocation requestingUserLocation  = getRequestingUserLocation(username);
        return getNearbyLocations(requestingUserLocation.getLatitude(), requestingUserLocation.getLongitude(), username);

    }

    private UserLocation getRequestingUserLocation(String username){
        DocumentSnapshot requestingUserLocationDocSnap = getUserLocationSnapshot(username);
        UserLocation requestingUserLocation = requestingUserLocationDocSnap.toObject(UserLocation.class);
        if(requestingUserLocation == null)
            throw new InvalidUserLocationDetailsException("Couldn't get user's location");
        return requestingUserLocation;
    }

    private List<UserLocation> getNearbyLocations(double latitude, double longitude, String requestingUser){
        double latDistance = 100 / EARTH_RADIUS;
        double lonDistance = 100 / (EARTH_RADIUS * Math.cos(Math.toRadians(latitude)));

        double latMin = latitude - Math.toDegrees(latDistance);
        double latMax = latitude + Math.toDegrees(latDistance);
        double lonMin = longitude - Math.toDegrees(lonDistance);
        double lonMax = longitude + Math.toDegrees(lonDistance);

        System.out.println("Latitude Range: " + latMin + " to " + latMax);
        System.out.println("Longitude Range: " + lonMin + " to " + lonMax);

        List<String> friendsOfRequestingUser = firebaseUserService.returnFriendsList(requestingUser);
        System.out.println("Friends of "+ requestingUser);
        for(String s : friendsOfRequestingUser)
            System.out.println(s);



        List<UserLocation> locations = new ArrayList<>();
        Query query = getCollectionReference()
                .whereGreaterThanOrEqualTo("latitude", latMin)
                .whereLessThanOrEqualTo("latitude", latMax)
                .whereGreaterThanOrEqualTo("longitude", lonMin)
                .whereLessThanOrEqualTo("longitude", lonMax);

        try {
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            QuerySnapshot snapshot = querySnapshot.get();
            System.out.println("Query result size: " + snapshot.getDocuments().size());
            for (DocumentSnapshot docSnap : querySnapshot.get().getDocuments()) {
                System.out.println("Document ID: " + docSnap.getId());
                UserLocation location = docSnap.toObject(UserLocation.class);
                if (location != null && calculateHaversineDistance(latitude, longitude, location.getLatitude(), location.getLongitude()) <= 100 && !docSnap.getId().equals(requestingUser)) {
                    location.setUsername(docSnap.getId());
                    if(checkIfUserCanBeShown(friendsOfRequestingUser, docSnap.getId()))
                        locations.add(location);
                    System.out.println("Added Location: " + location);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException( e);
        }
        return locations;
    }

    private boolean checkIfUserCanBeShown(List<String> friends, String nearbyUser){
        Discoverability discoverability = firebaseUserService.checkDiscoverabilityOfUser(nearbyUser);
        return switch (discoverability) {
            case EVERYONE -> true;
            case FRIENDS -> checkIfFriends(friends, nearbyUser);
            case NOONE -> false;
        };
    }

    private boolean checkIfFriends(List<String> friends, String nearbyUser){
        for(String s : friends)
            if(s.equals(nearbyUser)) {
                return true;
            }
        return false;
    }





    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    private CollectionReference getCollectionReference() {
        return firestore.collection("UserLocations");
    }

    private void checkData(String username, double latitude, double longitude){
        if(username.isEmpty())
            throw new InvalidUserLocationDetailsException("Invalid username");
        if (latitude < -90 || latitude > 90) {
            throw new InvalidUserLocationDetailsException("Invalid latidude");
        }
        if (longitude < -180 || longitude > 180) {
            throw new InvalidUserLocationDetailsException("Invalid longitude");
        }
    }
}
