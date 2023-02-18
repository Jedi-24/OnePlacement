package com.Jedi.OnePlacementServer.exceptions;

public class LoginException extends RuntimeException{

    public LoginException() {
    }

    public LoginException(String message) {
        super(message);
    }
}
