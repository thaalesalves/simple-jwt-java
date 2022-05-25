package com.signicat.interview.domain.exception;

public class PasswordNotStrongEnoughException extends RuntimeException {

    public PasswordNotStrongEnoughException(String msg) {
        super(msg);
    }
}
