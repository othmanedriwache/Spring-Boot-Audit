package com.auditPersist.exeptions.handlers;

import com.auditPersist.exeptions.*;
import com.auditPersist.response.Response;
import org.springframework.http.ResponseEntity;
public interface CustomExceptionHandler {

    ResponseEntity<Response> handleResponseException(ResponseException e);

    ResponseEntity<Response> handleNoRecordException(NoRecordException e);

    ResponseEntity<Response> handleNullPointerException(NullParameterException e);

    ResponseEntity<Response> handleDeleteExceptionException(DeleteException e);

    ResponseEntity<Response> handleNoValidDataException(NoValidDataException e);
}
