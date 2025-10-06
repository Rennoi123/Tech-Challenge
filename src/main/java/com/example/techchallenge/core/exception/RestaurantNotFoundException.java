package com.example.techchallenge.core.exception;

public class RestaurantNotFoundException extends RuntimeException{

    public RestaurantNotFoundException(String message) {
        super(message);
    }
}