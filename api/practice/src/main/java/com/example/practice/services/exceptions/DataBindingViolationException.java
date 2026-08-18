package com.example.practice.services.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class DataBindingViolationException extends DataIntegrityViolationException{
    

    public DataBindingViolationException(String message){
        super(message);
    }
}
