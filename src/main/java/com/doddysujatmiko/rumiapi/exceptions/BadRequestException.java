package com.doddysujatmiko.rumiapi.exceptions;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String s) {
        super(s);
    }
}
