package com.security.SecureApp.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.security.SecureApp.controller.NoUserFoundException;
import org.springframework.http.HttpStatus;

import org.springframework.beans.TypeMismatchException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;
@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ResponseBody    public String requestHandlingNoHandlerFound() {
        return "No Such URL Exists";
    }
    @ExceptionHandler(TypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody    public String typeMismatch() {
        return "Please Enter Proper Values";
    }
    @ExceptionHandler(NoUserFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody    public String noSuchUser() {
        return "No Such User Exists";
    }
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String internalServerError() {
        return "forward:/home/error";
    }

    @ExceptionHandler(SQLServerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String sqlServerException() {
        return "SQL Server Error";
    }

}