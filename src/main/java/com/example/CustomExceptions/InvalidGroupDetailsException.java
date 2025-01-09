package com.example.CustomExceptions;

public class InvalidGroupDetailsException extends RuntimeException{
    public InvalidGroupDetailsException(String message){ super(message); }
}
