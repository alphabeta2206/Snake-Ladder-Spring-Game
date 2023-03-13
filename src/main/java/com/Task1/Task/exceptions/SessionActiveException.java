package com.Task1.Task.exceptions;

public class SessionActiveException extends RuntimeException{
    public SessionActiveException(String message) { super(message+ "Active"); }
}
