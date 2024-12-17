package com.example.BikeChat.Group;

import com.example.CustomExceptions.InvalidGroupDetailsException;
import com.google.api.client.util.DateTime;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class GroupService {
    @Autowired
    private Firestore firestore;

    public GroupService(Firestore firestore){ this.firestore = firestore; }


    public void createGroup(Group group){
        try {
            marksChecker(group);
            ApiFuture<DocumentReference> result = firestore.collection("Groups").add(group);
            System.out.println("Result: " + result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void joinGroup(String groupID, String userID){
        DocumentReference groupDocumentReference = getGroupByID(groupID);
        ApiFuture<WriteResult> future = groupDocumentReference.update("participantsID", FieldValue.arrayUnion(userID));
        futureCheckerForWriteResults(future);
    }



    private DocumentReference getGroupByID(String groupID){
        DocumentReference docRef = firestore.collection("Groups").document(groupID);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try{
            DocumentSnapshot groupDocument = future.get();
            if(groupDocument.exists())
                return docRef;
            else
                throw new InvalidGroupDetailsException("Group does not exist");
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void marksChecker(Group group){
        if(group.getHostID().isEmpty() || group.getHostID() == null)
            throw new InvalidGroupDetailsException("Host ID Invalid!");
        if(group.getParticipantsID().isEmpty() || group.getParticipantsID()==null)
            throw new InvalidGroupDetailsException("Participants list cannot be empty!");
        if(!group.getActive())
            throw new InvalidGroupDetailsException("Upon creation group must be active!");
        if(group.getCreationDate() == null)
            throw new InvalidGroupDetailsException("Invalid creation date!");
    }

    private void futureCheckerForWriteResults(ApiFuture<WriteResult> future){
        try {
            WriteResult writeResult = future.get();
            System.out.println("Update time: " + writeResult.getUpdateTime());
        } catch (ExecutionException e) {
            System.err.println("Error updating document: " + e.getCause().getMessage());
            throw new RuntimeException("Failed to update participants list: " + e.getCause().getMessage(), e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Update operation interrupted", e);
        }
    }
}





















