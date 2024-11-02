package com.example.CustomExceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String userID){
        super("User with ID " + userID + " not found!");
    }
}
