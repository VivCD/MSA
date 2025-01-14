package com.example.BikeChat.User.UserGroups;

import com.example.BikeChat.Group.GroupInfo.GroupService;
import com.example.CustomExceptions.InvalidUserGroupsDetailsException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class UserGroupsService {
    @Autowired
    private Firestore firestore;

    public void addGroup(String groupID, String groupName, String username){
        DocumentReference userGroupsDocRef = getUserGroupsDocumentReference(username);
        if(userGroupsDocRef == null) {
            userGroupsDocRef = createUserGroupsDoc(username);
        }
        Map<String, String> group = new HashMap<>();
        group.put("groupId", groupID);
        group.put("groupName", groupName);
        ApiFuture<WriteResult> future = userGroupsDocRef.update("groups", FieldValue.arrayUnion(group));
        try{
            System.out.println("User groups updated at: " + future.get().getUpdateTime());
        } catch ( ExecutionException | InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public void deleteGroup(String username, String groupId){
        DocumentReference userGroupDocRef = getUserGroupsDocumentReference(username);
        if(userGroupDocRef == null)
            throw new InvalidUserGroupsDetailsException("User groups document doesn't exist.");
        ApiFuture <WriteResult> future = userGroupDocRef.update("groupIds", FieldValue.arrayRemove(groupId));
        try{
            System.out.println("User groups updated at: " + future.get().getUpdateTime());
        } catch ( ExecutionException | InterruptedException e){
            throw new RuntimeException("Couldn't update user's groups.");
        }
    }

    public List<String> getGroupsOfUser(String username){
        try{
            DocumentSnapshot userGroupDocRef = getUserGroupsDocumentSnapshot(username);
            return (List<String>) userGroupDocRef.get("groupIds");
        } catch (Exception e){
            throw new RuntimeException("Couldn't get the groups of user");
        }
    }

    private DocumentReference createUserGroupsDoc(String username){
        UserGroups userGroups = new UserGroups();
        userGroups.setUsername(username);
        userGroups.setGroupIds(new ArrayList<>());
        DocumentReference docRef = firestore.collection("UserGroups").document(username);

        ApiFuture<WriteResult> future = docRef.set(userGroups);
        try {
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Couldn't create UserGroups document for username: " + username);
        }

        return docRef;
    }

    private DocumentReference getUserGroupsDocumentReference(String username){
        DocumentReference docRef = firestore.collection("UserGroups").document(username);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try{
            DocumentSnapshot docSnap = future.get();
            if(docSnap.exists())
                return docRef;
            else
                return null;
        }catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to get the document reference for the username");
        }
    }

    private DocumentSnapshot getUserGroupsDocumentSnapshot(String username){
        DocumentReference docRef = firestore.collection("UserGroups").document(username);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try{
            DocumentSnapshot docSnap = future.get();
            if(docSnap.exists())
                return docSnap;
            else
                throw new InvalidUserGroupsDetailsException("UserGroups document doesn't exist!");
        }catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to get the document snapshot for the username");
        }
    }
}
