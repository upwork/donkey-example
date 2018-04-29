package com.example.services.zoo.exception;

import javax.annotation.Generated;

@Generated("com.upwork.donkey.example.spring.ClientGenerator, donkey-example:1.0-SNAPSHOT")
public abstract class ZooException extends RuntimeException {
    private final int statusCode;
    private final int errorCode;

    public ZooException(int statusCode, int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}