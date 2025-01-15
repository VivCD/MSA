package com.example.BikeChat.Firebase;

import com.example.BikeChat.SimpleClasses.Enums.Discoverability;
import com.example.BikeChat.User.UserInfo.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseUserService {

    private final Firestore firestore;

    public FirebaseUserService(Firestore firestore) {
        this.firestore = firestore;
    }


    public User getUserByUsername(String username) {
        try {
            Query query = firestore.collection("users").whereEqualTo("username", username);
            ApiFuture<QuerySnapshot> future = query.get();
            QuerySnapshot snapshot = future.get();

            if (!snapshot.isEmpty()) {
                System.out.println("Document data: " + snapshot.getDocuments().get(0).getData());
                return snapshot.getDocuments().get(0).toObject(User.class);
            } else {
                System.err.println("No user found with username: " + username);
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching user by username: " + e.getMessage(), e);
        }
    }






    public void addUser(User user) {
        DocumentReference docRef = firestore.collection("users").document(String.valueOf(user.getUserID()));
        ApiFuture<WriteResult> writeResult = docRef.set(user);
        try {
            // Wait for the result to confirm success and print it
            WriteResult result = writeResult.get();
            System.out.println("User added successfully at " + result.getUpdateTime());
        } catch (Exception e) {
            System.err.println("Error adding user: " + e.getMessage());
        }
    }


    public void saveUser(User user) {
        try {
            DocumentReference docRef = firestore.collection("users").document(user.getUserID());
            ApiFuture<WriteResult> future = docRef.set(user);
            future.get();
            System.out.println("User saved successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Error saving user: " + e.getMessage(), e);
        }
    }

    public User getUserByID(String userID) {
        try {
            DocumentReference docRef = firestore.collection("users").document(userID);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(User.class);
            } else {
                throw new RuntimeException("User with ID " + userID + " not found.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching user by ID: " + e.getMessage(), e);
        }
    }

    public List<User> getAllUsers() {
        try {
            List<User> users = new ArrayList<>();
            CollectionReference usersCollection = firestore.collection("users");
            ApiFuture<QuerySnapshot> future = usersCollection.get();
            QuerySnapshot querySnapshot = future.get();
            for (QueryDocumentSnapshot document : querySnapshot) {
                users.add(document.toObject(User.class));
            }
            return users;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all users: " + e.getMessage(), e);
        }
    }


    private DocumentSnapshot findUserByUsername(String username) throws Exception {

        Query query = firestore.collection("users").whereEqualTo("username", username);
        ApiFuture<QuerySnapshot> future = query.get();
        QuerySnapshot snapshot = future.get();

        if (!snapshot.isEmpty()) {
            return snapshot.getDocuments().get(0);
        } else {
            System.err.println("No user found with username: " + username);
            throw new RuntimeException("User with username " + username + " not found.");
        }
    }


    private void updatePendingRequests(DocumentReference userRef, String senderUsername) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> future = userRef.update("pendingRequests", FieldValue.arrayUnion(senderUsername));
        future.get();
    }

    public void sendFriendRequest(String senderUsername, String receiverUsername) {
        try {
            DocumentSnapshot receiverDocument = findUserByUsername(receiverUsername);
            updatePendingRequests(receiverDocument.getReference(), senderUsername);
            System.out.println("Friend request sent successfully from " + senderUsername + " to " + receiverUsername);
        } catch (Exception e) {
            System.err.println("Error sending friend request: " + e.getMessage());
        }
    }

    private void updateFriendsList(DocumentReference userRef, String senderUsername) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> future = userRef.update("friends", FieldValue.arrayUnion(senderUsername));
        future.get();
    }

    private void removeFromPendingRequests(DocumentReference userRef, String senderUsername) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> future = userRef.update("pendingRequests", FieldValue.arrayRemove(senderUsername));
        future.get();
    }

    public void acceptFriendRequest(String senderUsername, String receiverUsername) {
        try {
            DocumentSnapshot receiverDocument = findUserByUsername(receiverUsername);
            DocumentSnapshot senderDocument = findUserByUsername(senderUsername);
            removeFromPendingRequests(receiverDocument.getReference(), senderUsername);
            updateFriendsList(senderDocument.getReference(), receiverUsername);
            updateFriendsList(receiverDocument.getReference(), senderUsername);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void removeFriendFromFriendsList(DocumentReference userRef, String usernameToBeDeleted) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> future = userRef.update("friends", FieldValue.arrayRemove(usernameToBeDeleted));
        future.get();
    }

    public void removeFriend(String friendWhoDeletes, String friendToBeDeleted) {
        try {
            DocumentSnapshot userWhoDeletes = findUserByUsername(friendWhoDeletes);
            DocumentSnapshot userToBeDeleted = findUserByUsername(friendToBeDeleted);
            removeFriendFromFriendsList(userWhoDeletes.getReference(), friendToBeDeleted);
            removeFriendFromFriendsList(userToBeDeleted.getReference(), friendWhoDeletes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(String userID) {
        try {
            DocumentReference docRef = firestore.collection("users").document(userID);
            ApiFuture<WriteResult> future = docRef.delete();
            future.get();
            System.out.println("User deleted successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }



    public List<String> returnFriendsList(String username) {
        try {
            DocumentSnapshot user = findUserByUsername(username);
            return (List<String>) user.get("friends");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateLocationDiscoverability(String username, Discoverability discoverability){
        try{
            DocumentSnapshot docSnap = findUserByUsername(username);
            DocumentReference docRef = docSnap.getReference();
            ApiFuture<WriteResult> futureDiscoverability = docRef.update("locationDiscoverability", discoverability);

        } catch (Exception e) {
            throw new RuntimeException("Couldn't update location discoverability");
        }

    }
    public Discoverability checkDiscoverabilityOfUser(String username){
        try{
            DocumentSnapshot user = findUserByUsername(username);
            String s = (String) user.get("locationDiscoverability");
            return switch (s) {
                case "EVERYONE" -> Discoverability.EVERYONE;
                case "FRIENDS" -> Discoverability.FRIENDS;
                case "NOONE" -> Discoverability.NOONE;
                default -> throw new IllegalStateException("Unexpected value: " + s);
            };

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void updateBio(String username, String bio){
        try{
            DocumentSnapshot docSnap = findUserByUsername(username);
            DocumentReference docRef = docSnap.getReference();
            ApiFuture<WriteResult> futureDiscoverability = docRef.update("bio", bio);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateEmail(String username, String email){
        try{
            DocumentSnapshot docSnap = findUserByUsername(username);
            DocumentReference docRef = docSnap.getReference();
            ApiFuture<WriteResult> futureDiscoverability = docRef.update("email", email);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void updateProfilePicture(String username, String profilePicture){
        try{
            DocumentSnapshot docSnap = findUserByUsername(username);
            DocumentReference docRef = docSnap.getReference();
            ApiFuture<WriteResult> futureDiscoverability = docRef.update("profilePictureUrl", profilePicture);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
