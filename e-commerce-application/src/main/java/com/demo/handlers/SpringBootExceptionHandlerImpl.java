package com.demo.handlers;

import com.demo.response.Response;
import com.demo.response.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * The type Spring boot exception handler.
 */
@ControllerAdvice
@AllArgsConstructor
public class SpringBootExceptionHandlerImpl implements SpringBootExceptionHandler {




    /**
     * Handles global exceptions such as NullPointerException, IllegalAccessError, etc.
     *
     * @param ex the Exception
     * @return the ResponseEntity with an appropriate error response
     */
    @ExceptionHandler({
            NullPointerException.class,
            IllegalAccessError.class,
            IllegalAccessException.class,
            IllegalStateException.class,
            Exception.class
    })
    public ResponseEntity<Response> handleGlobalExceptions(Exception ex) {
        return ResponseService.bad_request("General Exception", ex.getMessage());
    }
}