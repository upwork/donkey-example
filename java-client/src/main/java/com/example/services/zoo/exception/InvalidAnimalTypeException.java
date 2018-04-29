package com.example.services.zoo.exception;

import javax.annotation.Generated;

@Generated("com.upwork.donkey.example.spring.ClientGenerator, donkey-example:1.0-SNAPSHOT")
public class InvalidAnimalTypeException extends ZooException {
    public static final int STATUS_CODE = 400;
    public static final int ERROR_CODE = 1;

    public InvalidAnimalTypeException() {
        super(STATUS_CODE, ERROR_CODE, null, null);
    }
}