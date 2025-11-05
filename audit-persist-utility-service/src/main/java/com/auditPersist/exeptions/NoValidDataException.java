package com.auditPersist.exeptions;

public class NoValidDataException extends RuntimeException {

    public NoValidDataException(String message) {
        super(message);
    }

    public NoValidDataException(String message, Throwable cause) {
        super(message, cause);
    }

}
