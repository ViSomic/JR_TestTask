package com.game.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class EntityExceptionHandler {

    @ExceptionHandler (value = {EntityNotFoundException.class})
    @ResponseStatus (value = HttpStatus.NOT_FOUND)
    public String resourceNotFoundException (EntityNotFoundException exc) {
        return "Not found resource" + exc.getMessage();
    }

    @ExceptionHandler (value = {HttpClientErrorException.class})
    @ResponseStatus (value = HttpStatus.BAD_REQUEST)
    public String resourceNotFoundException (HttpClientErrorException exc) {
        return exc.getMessage();
    }
}
