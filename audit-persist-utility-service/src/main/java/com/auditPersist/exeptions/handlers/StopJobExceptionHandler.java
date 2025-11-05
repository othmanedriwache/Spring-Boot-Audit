package com.auditPersist.exeptions.handlers;


import com.auditPersist.exeptions.StopJobException;

public interface StopJobExceptionHandler {

    String handleStopJobException(StopJobException e);


}
