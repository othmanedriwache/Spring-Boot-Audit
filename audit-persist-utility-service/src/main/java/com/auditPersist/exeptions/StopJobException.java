package com.auditPersist.exeptions;

public class StopJobException extends RuntimeException {

    public StopJobException(String message) {
        super(message);
    }

    public StopJobException(String message, Throwable cause) {
        super(message, cause);
    }

}
