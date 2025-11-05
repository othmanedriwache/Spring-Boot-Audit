package com.auditPersist.exeptions.handlers;

import com.auditPersist.response.Response;
import com.auditPersist.response.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@AllArgsConstructor
public class SpringBootExceptionHandlerImpl implements SpringBootExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<Response> handleMultipartException(MultipartException ex) {
        return ResponseService.bad_request("Please select a file");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ResponseService.bad_request("INVALID DATA", getValidationExceptionsErrors(ex));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Response> handleConstraintViolation(ConstraintViolationException ex) {
        try {
            return ResponseService.bad_request("contain violation", ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseService.bad_request(ex.getMessage());
        }
    }

    private Map<String, List<String>> getValidationExceptionsErrors(BindException ex) {
        Map<String, List<String>> entityError = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            List<String> fieldErrors = new ArrayList<>();
            if (entityError.containsKey(error.getField()))
                fieldErrors = entityError.get(error.getField());
            fieldErrors.add(error.getDefaultMessage());
            entityError.put(error.getField(), fieldErrors);
        });
        return entityError;
    }

    @ExceptionHandler(
            {
                    NullPointerException.class,
                    IllegalAccessError.class,
                    IllegalAccessException.class,
                    IllegalStateException.class,
                    Exception.class
            })
    public ResponseEntity<Response> handleGlobalExceptions(Exception ex) {
        ex.printStackTrace();

        // Or use logger (better practice)
        // log.error("Exception occurred: ", ex);

        System.out.println("Error message: " + ex.getMessage());
        System.out.println("Error class: " + ex.getClass().getName());
        return ResponseService.bad_request("General Exception",ex.getMessage());
    }
}