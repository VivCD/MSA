package com.example.BikeChat.Group.GroupInfo;

import com.example.BikeChat.User.UserGroups.UserGroupsService;
import com.example.CustomExceptions.InvalidGroupDetailsException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class GroupService {
    @Autowired
    private Firestore firestore;
    @Autowired
    private UserGroupsService userGroupsService;

    public GroupService(Firestore firestore){ this.firestore = firestore; }




    public String createGroup(String username, String groupName){
        Group group = new Group();
        group = createDetails(username, groupName);
        String groupId;
        try{
            ApiFuture<DocumentReference> result = firestore.collection("Groups").add(group);
            DocumentReference docRef = result.get();
            groupId = docRef.getId();
            userGroupsService.addGroup(groupId, username);

            System.out.println("DocId: " + groupId);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return groupId;
    }

    public void joinGroup(String groupID, String username){
        DocumentReference groupDocumentReference = getGroupReferenceByID(groupID);
        ApiFuture<WriteResult> future = groupDocumentReference.update("participantsUsernames", FieldValue.arrayUnion(username));
        futureCheckerForWriteResults(future);
        userGroupsService.addGroup(groupID, username);
    }

    public void leaveGroup(String groupID, String username){
        DocumentReference groupDocumentReference = getGroupReferenceByID(groupID);
        ApiFuture<WriteResult> future = groupDocumentReference.update("participantsUsernames", FieldValue.arrayRemove(username));
        futureCheckerForWriteResults(future);
        userGroupsService.deleteGroup(username, groupID);
    }

    public List<Group> searchGroupByName(String groupName){
        List<Group> groups = new ArrayList<>();
        try{
            ApiFuture<QuerySnapshot> query = firestore.collection("Groups")
                    .whereEqualTo("groupName", groupName)
                    .get();
            QuerySnapshot querySnapshot = query.get();
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                Group group = document.toObject(Group.class);
                group.setGroupID(document.getId());

                groups.add(group);
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Couldn't search for groups by name.");
        }
        return groups;
    }

    public Group searchGroupByID(String groupId) {
        DocumentSnapshot docSnap = getGroupSnapshotByID(groupId);
        return docSnap.toObject(Group.class);
    }


    private DocumentReference getGroupReferenceByID(String groupID){
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

    private DocumentSnapshot getGroupSnapshotByID(String groupID){
        DocumentReference docRef = firestore.collection("Groups").document(groupID);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try{
            DocumentSnapshot groupDocument = future.get();
            if(groupDocument.exists())
                return groupDocument;
            else
                throw new InvalidGroupDetailsException("Group does not exist");
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Object> getMapOfParticipants(String groupID) {
        DocumentSnapshot documentSnapshot = getGroupSnapshotByID(groupID);
        return (List<Object>) documentSnapshot.get("participantsUsernames");
    }

    private Group createDetails(String username, String groupName){
        Group group = new Group();
        group.setCreatorUsername(username);
        List<String> usernames = new ArrayList<>();
        usernames.add(username);
        group.setParticipantsUsernames(usernames);
        group.setActive(true);
        group.setGroupName(groupName);
        return group;
    }


    private void marksChecker(Group group){
        if(group.getCreatorUsername().isEmpty() || group.getCreatorUsername() == null)
            throw new InvalidGroupDetailsException("Host ID Invalid!");
        if(group.getParticipantsUsernames().isEmpty() || group.getParticipantsUsernames()==null)
            throw new InvalidGroupDetailsException("Participants list cannot be empty!");
        if(!group.getActive())
            throw new InvalidGroupDetailsException("Upon creation group must be active!");
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





















