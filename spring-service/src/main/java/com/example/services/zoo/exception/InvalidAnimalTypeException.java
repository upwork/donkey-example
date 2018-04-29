package com.example.services.zoo.exception;

import javax.annotation.Generated;

@Generated("com.upwork.donkey.example.spring.ServiceGenerator, donkey-example:1.0-SNAPSHOT")
public class InvalidAnimalTypeException extends ZooException {
    private static final int STATUS_CODE = 400;
    private static final int ERROR_CODE = 1;

    public InvalidAnimalTypeException() {
        super(STATUS_CODE, ERROR_CODE, null, null);
    }
}