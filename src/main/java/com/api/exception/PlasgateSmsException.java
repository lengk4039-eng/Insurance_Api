package com.api.exception;

/** Thrown when an SMS could not be sent through the PlasGate gateway. */
public class PlasgateSmsException extends RuntimeException {

    public PlasgateSmsException(String message) {
        super(message);
    }

    public PlasgateSmsException(String message, Throwable cause) {
        super(message, cause);
    }
}
