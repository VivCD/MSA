package com.example.BikeChat.Group;

//import com.example.BikeChat.APIResponse.ApiResponse;
import com.example.BikeChat.CallLogs.CallLogService;
import com.example.CustomExceptions.InvalidCallException;
import com.example.CustomExceptions.InvalidGroupDetailsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    GroupService groupService;

    @Autowired
    CallLogService callLogService;



    @PostMapping("/createGroup")
    public ResponseEntity<String> createGroup(@RequestBody Group group){
        try{
            groupService.createGroup(group);
            return ResponseEntity.status(HttpStatus.CREATED).body("Group created successfully!");
        } catch(InvalidGroupDetailsException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred!");
        }
    }

    @PostMapping("/joinGroup")
    public ResponseEntity<String> joinGroup(@RequestParam String groupID, @RequestParam String userID){
        try{
            groupService.joinGroup(groupID, userID);
            return ResponseEntity.status(HttpStatus.OK).body("Group joined successfully!");
        }catch (InvalidGroupDetailsException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/leaveGroup")
    public ResponseEntity<String> leaveGroup(@RequestParam String groupID, @RequestParam String userID){
        try{
            groupService.leaveGroup(groupID, userID);
            return ResponseEntity.status(HttpStatus.OK).body("Group left successfully!");
        } catch (InvalidGroupDetailsException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/initiateCall")
    public ResponseEntity<String> initiateCall(@RequestParam String groupID){
        try{
            String callID = callLogService.initiateCall(groupID);
            return ResponseEntity.status(HttpStatus.CREATED).body("Call initiated successfully with ID: " + callID);
        } catch (InvalidGroupDetailsException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/leaveCall")
    public ResponseEntity<String> leaveCall(@RequestParam String callLogID, @RequestParam String userID){
        try{
            callLogService.leaveCall(callLogID, userID);
            return ResponseEntity.status(HttpStatus.OK).body("Call left successfully");
        } catch(InvalidCallException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
