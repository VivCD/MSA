package com.example.BikeChat.CallLogs;

import com.example.BikeChat.Group.GroupService;
import com.example.CustomExceptions.InvalidCallException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CallLogService {
    @Autowired
    private Firestore firestore;
    @Autowired
    private GroupService groupService;

    public String saveCallLog(CallLog callLog){
        ApiFuture<DocumentReference> result = firestore.collection("Logs").add(callLog);
        try {
            result.get();
            System.out.println("Call log saved with ID: " + callLog.getCallLogID());
            return callLog.getCallLogID();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }



    public String initiateCall(String groupID){
        CallLog callLog = new CallLog();
        callLog.setGroupID(groupID);

        List<Object> participantsMap = groupService.getMapOfParticipants(groupID);
        List<String> participants = objectToString(participantsMap);
        callLog.setParticipantsID(participants);

        Instant now = Instant.now();
        callLog.setStartTime(now);

        callLog.setActive(true);
        return saveCallLog(callLog);
    }



    public void leaveCall(String callLogID, String userID){

        List<Object> participantsMap = getMapOfParticipants(callLogID);
        List<String> participants = objectToString(participantsMap);
        if(participants.size() == 1){
            endCall(callLogID);
        }
        DocumentReference documentReference = getCallReferenceByID(callLogID);
        ApiFuture<WriteResult> future = documentReference.update("participantsID", FieldValue.arrayRemove(userID));

    }

    private void endCall(String callLogID){
        DocumentReference documentReference = getCallReferenceByID(callLogID);
        DocumentSnapshot documentSnapshot = getCallSnapshotByID(callLogID);

        Instant startTime = documentSnapshot.getTimestamp("startTime").toDate().toInstant();
        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        long durationInSeconds = duration.getSeconds();

        ApiFuture<WriteResult> future = documentReference.update("duration", durationInSeconds);

        future = documentReference.update("active", false);
        future = documentReference.update("endTime", endTime);

    }

    private List<String> objectToString(List<Object> objectToString) {
        List<String> stringList = new ArrayList<>();

        for (Object value : objectToString) {
            stringList.add(value.toString());
        }

        return stringList;
    }


    private DocumentSnapshot getCallSnapshotByID(String callLogID){
        DocumentReference docRef = firestore.collection("Logs").document(callLogID);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try{
            DocumentSnapshot documentSnapshot = future.get();
            if(documentSnapshot.exists())
                return documentSnapshot;
            else
                throw new InvalidCallException("Call doesn't exist1");
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private DocumentReference getCallReferenceByID(String callLogID){
        DocumentReference docRef = firestore.collection("Logs").document(callLogID);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try{
            DocumentSnapshot documentSnapshot = future.get();
            if(documentSnapshot.exists())
                return docRef;
            else
                throw new InvalidCallException("Call doesn't exist2");
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private List<String> getListOfParticipants(String callLogID){
        DocumentSnapshot documentSnapshot = getCallSnapshotByID(callLogID);
        List<String> participants;
        participants = documentSnapshot.get("participantsID", List.class);
        if(participants.isEmpty() || participants == null)
            return new ArrayList<>();
        else
            return participants;
    }

    public List<Object> getMapOfParticipants(String callLogID) {
        DocumentSnapshot documentSnapshot = getCallSnapshotByID(callLogID);

        List<Object> participantsMap = (List<Object>) documentSnapshot.get("participantsID");

        if (participantsMap == null || participantsMap.isEmpty()) {
            return new ArrayList<>();
        } else {
            return participantsMap;
        }
    }

}
