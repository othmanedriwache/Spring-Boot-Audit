package com.auditPersist.exeptions;

public class NullParameterException extends RuntimeException {

    public NullParameterException(String message) {
        super(message);
    }

    public NullParameterException(String message, Throwable cause) {
        super(message, cause);
    }

}
