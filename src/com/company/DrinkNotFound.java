package com.company;

public class DrinkNotFound extends Exception {
    public DrinkNotFound(String errorMessage) {
        super(errorMessage);
    }
}
