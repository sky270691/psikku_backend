package com.psikku.backend.exception;

public class TestResultException extends RuntimeException {

    public TestResultException(String message) {
        super(message);
    }

    public TestResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestResultException(Throwable cause) {
        super(cause);
    }
}
