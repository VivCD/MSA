package com.example.CustomExceptions;

public class InvalidUserLocationDetailsException extends RuntimeException{
    public InvalidUserLocationDetailsException(String message){ super(message);}
}
