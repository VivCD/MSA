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
public class FirebaseUserService {

    @Autowired
    private Firestore firestore;

    public FirebaseUserService(Firestore firestore) {
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

    private DocumentSnapshot findUserByUsername(String username) throws Exception {
        Query query = firestore.collection("users").whereEqualTo("username", username);
        ApiFuture<QuerySnapshot> future = query.get();
        QuerySnapshot snapshot = future.get();
        if (snapshot.isEmpty()) {
            throw new Exception("User with username " + username + " not found.");
        }
        return snapshot.getDocuments().get(0);
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

    private void updateFriendsList(DocumentReference userRef,String senderUsername) throws ExecutionException, InterruptedException{
        ApiFuture<WriteResult> future = userRef.update("friends", FieldValue.arrayUnion(senderUsername));
        future.get();
    }

    private void removeFromPendingRequests(DocumentReference userRef, String senderUsername) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> future = userRef.update("pendingRequests", FieldValue.arrayRemove(senderUsername));
        future.get();
    }

    public void acceptFriendRequest(String senderUsername, String receiverUsername){
        try{
            DocumentSnapshot receiverDocument = findUserByUsername(receiverUsername);
            DocumentSnapshot senderDocument = findUserByUsername(senderUsername);
            removeFromPendingRequests(receiverDocument.getReference(), senderUsername);
            updateFriendsList(senderDocument.getReference(), receiverUsername);
            updateFriendsList(receiverDocument.getReference(), senderUsername);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void removeFriendFromFriendsList(DocumentReference userRef, String usernameToBeDeleted) throws ExecutionException, InterruptedException{
        ApiFuture<WriteResult> future = userRef.update("friends", FieldValue.arrayRemove(usernameToBeDeleted));
        future.get();
    }

    public void removeFriend(String friendWhoDeletes, String friendToBeDeleted){
        try{
            DocumentSnapshot userWhoDeletes = findUserByUsername(friendWhoDeletes);
            DocumentSnapshot userToBeDeleted = findUserByUsername(friendToBeDeleted);
            removeFriendFromFriendsList(userWhoDeletes.getReference(), friendToBeDeleted);
            removeFriendFromFriendsList(userToBeDeleted.getReference(), friendWhoDeletes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> returnFriendsList(String username){
        try{
            DocumentSnapshot user = findUserByUsername(username);
            List<String> friends = (List<String>)user.get("friends");
            return friends;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
