package com.auditPersist.exeptions;

public class UnableToCastException extends RuntimeException {

    public UnableToCastException(String message) {
        super(message);
    }

    public UnableToCastException(String message, Throwable cause) {
        super(message, cause);
    }

}

