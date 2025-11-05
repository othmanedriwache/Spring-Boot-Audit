package com.auditPersist.exeptions.handlers;

import com.auditPersist.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;

public interface SpringBootExceptionHandler {


    ResponseEntity<Response> handleValidationExceptions(MethodArgumentNotValidException ex);

    ResponseEntity<Response> handleConstraintViolation(ConstraintViolationException ex);


}
