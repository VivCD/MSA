package com.example.BikeChat.Group;

import com.example.BikeChat.APIResponse.ApiResponse;
import com.example.CustomExceptions.InvalidGroupDetailsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    GroupService groupService;



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
            return ResponseEntity.status(HttpStatus.CREATED).body("Group joined successfully!");
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
}
