package com.example.BikeChat.Group.GroupInfo;


import com.example.BikeChat.APIResponse.ApiResponse;
import com.example.BikeChat.CallLogs.CallLogService;
import com.example.BikeChat.User.UserGroups.UserGroupsService;
import com.example.CustomExceptions.InvalidCallException;
import com.example.CustomExceptions.InvalidGroupDetailsException;
import com.google.api.client.util.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    GroupService groupService;
    @Autowired
    UserGroupsService userGroupsService;

    @Autowired
    CallLogService callLogService;



    @PostMapping("/createGroup")
    public ResponseEntity<String> createGroup(@RequestParam String username, @RequestParam String groupName){
        try{
            String groupId = groupService.createGroup(username, groupName);
            return ResponseEntity.status(HttpStatus.CREATED).body("Group created successfully with ID: " + groupId);
        } catch(InvalidGroupDetailsException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/joinGroup")
    public ResponseEntity<String> joinGroup(@RequestParam String groupID, @RequestParam String username){
        try{
            groupService.joinGroup(groupID, username);
            return ResponseEntity.status(HttpStatus.OK).body("Group joined successfully!");
        }catch (InvalidGroupDetailsException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/leaveGroup")
    public ResponseEntity<String> leaveGroup(@RequestParam String groupID, @RequestParam String username){
        try{
            groupService.leaveGroup(groupID, username);
            return ResponseEntity.status(HttpStatus.OK).body("Group left successfully!");
        } catch (InvalidGroupDetailsException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/initiateCall")
    public ResponseEntity<ApiResponse> initiateCall(@RequestParam String groupID) {
        try {
            String callID = callLogService.initiateCall(groupID);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Call initiated successfully with ID: " + callID, true));
        } catch (InvalidGroupDetailsException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(ex.getMessage(), false));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ex.getMessage(), false));
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

    @PostMapping("/endCall")
    public ResponseEntity<String> endCall(@RequestParam String callLogID) {
        try {
            callLogService.leaveCall(callLogID, "FORCE_END");
            return ResponseEntity.status(HttpStatus.OK).body("Call ended successfully");
        } catch (InvalidCallException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/getByName")
    public ResponseEntity<?> getByName(@RequestParam String groupName){
        try{
            List<Group> groups = groupService.searchGroupByName(groupName);
            if (groups.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No groups found with the name: " + groupName);
            }
            return ResponseEntity.ok(groups);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/getById")
    public ResponseEntity<?> getById(@RequestParam String groupId){
        try{
            Group group = groupService.searchGroupByID(groupId);
            return ResponseEntity.ok(group);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
    @GetMapping("/getUserGroups")
    public ResponseEntity<?> getUserGroups(@RequestParam String username) {
        try {
            List<Map<String, String>> userGroups = userGroupsService.getGroupsOfUser(username);
            return ResponseEntity.ok(userGroups);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
