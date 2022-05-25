package com.signicat.interview.domain.exception;

public class UserGroupNotFoundException extends RuntimeException {

    public UserGroupNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserGroupNotFoundException(String msg) {
        super(msg);
    }
}
