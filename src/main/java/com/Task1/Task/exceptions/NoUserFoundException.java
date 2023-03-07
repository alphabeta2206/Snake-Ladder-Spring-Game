package com.Task1.Task.exceptions;

public class NoUserFoundException extends RuntimeException{
    NoUserFoundException(String message){
        super(message);
    }
}
