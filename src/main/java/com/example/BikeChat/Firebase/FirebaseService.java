package com.example.BikeChat.Firebase;

import com.example.BikeChat.User.User;
import com.example.CustomExceptions.UserNotFoundException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

    @Autowired
    private Firestore firestore;

    public FirebaseService(Firestore firestore) {
        this.firestore = firestore;
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


    public void saveUser(User user){
        DocumentReference docRef =
                firestore.collection("users").document(user.getUserID().toString());
        ApiFuture<WriteResult> result = docRef.set(user);


    }

    public User getUserByID(String userID){
        DocumentReference docRef = firestore.collection("users").document(userID);
        ApiFuture<DocumentSnapshot> future = docRef.get();

        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(User.class);
            }
            else {
                throw new UserNotFoundException(userID);
            }
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        CollectionReference usersCollection = firestore.collection("users");

        ApiFuture<QuerySnapshot> future = usersCollection.get();

        try {
            QuerySnapshot querySnapshot = future.get();
            for(QueryDocumentSnapshot document : querySnapshot){
                User user = document.toObject(User.class);
                users.add(user);
            }
            return users;

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendFriendRequest(String senderId, String receiverId) {
        DocumentReference receiverRef = firestore.collection("users").document(receiverId);
        receiverRef.update("pendingRequests", FieldValue.arrayUnion(senderId));
    }

    public void acceptFriendRequest(String userId, String friendId) {
        // Add to friends list
        DocumentReference userRef = firestore.collection("users").document(userId);
        DocumentReference friendRef = firestore.collection("users").document(friendId);

        userRef.update("friends", FieldValue.arrayUnion(friendId));
        friendRef.update("friends", FieldValue.arrayUnion(userId));

        // Remove from pending requests
        userRef.update("pendingRequests", FieldValue.arrayRemove(friendId));
    }
}
