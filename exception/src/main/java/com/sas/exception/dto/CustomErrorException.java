package com.sas.exception.dto;

public class CustomErrorException extends RuntimeException{

    public CustomErrorException(String message) {
        super(message);
    }
}