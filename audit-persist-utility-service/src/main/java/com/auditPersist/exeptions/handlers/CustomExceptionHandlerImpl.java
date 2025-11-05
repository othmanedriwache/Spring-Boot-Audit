package com.auditPersist.exeptions.handlers;


import com.auditPersist.exeptions.*;
import com.auditPersist.response.Response;
import com.auditPersist.response.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@AllArgsConstructor
@ControllerAdvice
public class CustomExceptionHandlerImpl implements CustomExceptionHandler {

    @ExceptionHandler(value = ResponseException.class)
    public ResponseEntity<Response> handleResponseException(ResponseException e) {
        return ResponseService.bad_request(e.getMessage());
    }

    @ExceptionHandler(value = NoRecordException.class)
    public ResponseEntity<Response> handleNoRecordException(NoRecordException e) {
        return ResponseService.bad_request("No records found  : " + e.getMessage() + " Please check back later.");
    }

    @ExceptionHandler(value = NullParameterException.class)
    public ResponseEntity<Response> handleNullPointerException(NullParameterException e) {
        return ResponseService.bad_request("Parameter  : " + e.getMessage() + " have no value");
    }

    @ExceptionHandler(value = DeleteException.class)
    public ResponseEntity<Response> handleDeleteExceptionException(DeleteException e) {
        return ResponseService.bad_request("Cant Delete   : " + e.getMessage() + " Contact Admin For More Information");
    }

    @ExceptionHandler(value = NoValidDataException.class)
    public ResponseEntity<Response> handleNoValidDataException(NoValidDataException e) {
        return ResponseService.bad_request(e.getMessage());
    }
}
