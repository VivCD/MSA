package com.example.CustomExceptions;

public class InvalidUserGroupsDetailsException extends RuntimeException{
    public InvalidUserGroupsDetailsException(String message){
        super(message);
    }
}
