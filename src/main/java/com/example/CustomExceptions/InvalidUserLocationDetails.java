package com.example.CustomExceptions;

public class InvalidUserLocationDetails extends RuntimeException{
    public InvalidUserLocationDetails(String message){ super(message);}
}
