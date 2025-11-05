package com.auditPersist.exeptions.handlers;


import com.auditPersist.exeptions.StopJobException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@AllArgsConstructor
@ControllerAdvice
public class StopJobExceptionHandlerImpl implements StopJobExceptionHandler {

    @ExceptionHandler(value = StopJobException.class)
    public String handleStopJobException(StopJobException e) {
        return "JOB_STOPPED_SUCCESSFULLY";
    }

}
