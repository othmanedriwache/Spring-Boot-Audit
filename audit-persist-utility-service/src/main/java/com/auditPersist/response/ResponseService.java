package com.auditPersist.response;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public interface ResponseService {
    static ResponseEntity<Response> success() {
        return new ResponseEntity(Response.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .developerStatus(ResponseStatus.SUCCESS)
                .build(), HttpStatus.OK);
    }

    static ResponseEntity<Response> success(Object data) {
        return new ResponseEntity(Response.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .data(data)
                .developerStatus(ResponseStatus.SUCCESS)
                .build(), HttpStatus.OK);
    }

    static ResponseEntity<Response> success(String message) {
        return new ResponseEntity(Response.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .developerStatus(ResponseStatus.SUCCESS)
                .build(), HttpStatus.OK);
    }

    static ResponseEntity<Response> success(String message, Object data) {
        return new ResponseEntity(Response.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .developerStatus(ResponseStatus.SUCCESS)
                .build(), HttpStatus.OK);
    }

    static ResponseEntity<Response> bad_request() {
        return new ResponseEntity(Response.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .developerStatus(ResponseStatus.BAD_REQUEST)
                .build(), HttpStatus.BAD_REQUEST);
    }

    static ResponseEntity<Response> bad_request(String message) {
        return new ResponseEntity(Response.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .developerStatus(ResponseStatus.BAD_REQUEST)
                .build(), HttpStatus.BAD_REQUEST);
    }

    static ResponseEntity<Response> bad_request(Object data) {
        return new ResponseEntity(Response.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .data(data)
                .developerStatus(ResponseStatus.BAD_REQUEST)
                .build(), HttpStatus.BAD_REQUEST);
    }

    static ResponseEntity<Response> bad_request(String message, Object data) {
        return new ResponseEntity(Response.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .data(data)
                .developerStatus(ResponseStatus.BAD_REQUEST)
                .build(), HttpStatus.BAD_REQUEST);
    }

    static ResponseEntity<Response> warning() {
        return new ResponseEntity(Response.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.NOT_ACCEPTABLE)
                .statusCode(HttpStatus.NOT_ACCEPTABLE.value())
                .developerStatus(ResponseStatus.WARNING)
                .build(), HttpStatus.BAD_REQUEST);

    }
}
