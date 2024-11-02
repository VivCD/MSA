package com.example.BikeChat.Firebase;

import com.example.BikeChat.User.User;
import com.example.CustomExceptions.UserNotFoundException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.Document;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

    @Autowired
    private Firestore firestore;


    public void saveUser(User user){
        DocumentReference docRef =
                firestore.collection("users").document(user.getUserID().toString());
        ApiFuture<WriteResult> result = docRef.set(user);


    }

    public User getUser(String userID){
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
}
