package com.example.CustomExceptions;

public class InvalidCallException extends RuntimeException {
    public InvalidCallException(String message){ super(message); }
}
