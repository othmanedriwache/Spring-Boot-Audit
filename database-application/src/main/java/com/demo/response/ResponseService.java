package com.demo.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

/**
 * The interface Response service for building consistent API responses.
 */
public interface ResponseService {

    /**
     * Returns a successful response with an OK status and no data or message.
     *
     * @return the ResponseEntity with a success status
     */
    static ResponseEntity<Response> success() {
        return ResponseEntity.ok(Response.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .developerStatus(ResponseStatus.SUCCESS)
                .build());
    }

    /**
     * Returns a successful response with an OK status and the provided data.
     *
     * @param data the data to include in the response
     * @return the ResponseEntity with a success status and data
     */
    static ResponseEntity<Response> success(Object data) {
        return ResponseEntity.ok(Response.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .data(data)
                .developerStatus(ResponseStatus.SUCCESS)
                .build());
    }

    /**
     * Returns a successful response with an OK status and the provided message.
     *
     * @param message the message to include in the response
     * @return the ResponseEntity with a success status and message
     */
    static ResponseEntity<Response> success(String message) {
        return ResponseEntity.ok(Response.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .developerStatus(ResponseStatus.SUCCESS)
                .build());
    }

    /**
     * Returns a successful response with an OK status, the provided message, and the provided data.
     *
     * @param message the message to include in the response
     * @param data    the data to include in the response
     * @return the ResponseEntity with a success status, message, and data
     */
    static ResponseEntity<Response> success(String message, Object data) {
        return ResponseEntity.ok(Response.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .developerStatus(ResponseStatus.SUCCESS)
                .build());
    }

    /**
     * Returns a bad request response with a BAD_REQUEST status and no data or message.
     *
     * @return the ResponseEntity with a bad request status
     */
    static ResponseEntity<Response> bad_request() {
        return ResponseEntity.badRequest().body(Response.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .developerStatus(ResponseStatus.BAD_REQUEST)
                .build());
    }

    /**
     * Returns a bad request response with a BAD_REQUEST status and the provided message.
     *
     * @param message the message to include in the response
     * @return the ResponseEntity with a bad request status and message
     */
    static ResponseEntity<Response> bad_request(String message) {
        return ResponseEntity.badRequest().body(Response.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .developerStatus(ResponseStatus.BAD_REQUEST)
                .build());
    }

    /**
     * Returns a bad request response with the specified status and the provided message.
     *
     * @param message    the message to include in the response
     * @param httpStatus the HTTP status to use
     * @return the ResponseEntity with a bad request status and message
     */
    static ResponseEntity<Response> bad_request(String message, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(Response.builder()
                .timeStamp(LocalDateTime.now())
                .status(httpStatus)
                .statusCode(httpStatus.value())
                .message(message)
                .developerStatus(ResponseStatus.BAD_REQUEST)
                .build());
    }

    /**
     * Returns a bad request response with a BAD_REQUEST status and the provided data.
     *
     * @param data the data to include in the response
     * @return the ResponseEntity with a bad request status and data
     */
    static ResponseEntity<Response> bad_request(Object data) {
        return ResponseEntity.badRequest().body(Response.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .data(data)
                .developerStatus(ResponseStatus.BAD_REQUEST)
                .build());
    }

    /**
     * Returns a bad request response with a BAD_REQUEST status, the provided message, and the provided data.
     *
     * @param message the message to include in the response
     * @param data    the data to include in the response
     * @return the ResponseEntity with a bad request status, message, and data
     */
    static ResponseEntity<Response> bad_request(String message, Object data) {
        return ResponseEntity.badRequest().body(Response.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .data(data)
                .developerStatus(ResponseStatus.BAD_REQUEST)
                .build());
    }

    /**
     * Returns a warning response with a NOT_ACCEPTABLE status.
     *
     * @return the ResponseEntity with a warning status
     */
    static ResponseEntity<Response> warning() {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Response.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.NOT_ACCEPTABLE)
                .statusCode(HttpStatus.NOT_ACCEPTABLE.value())
                .developerStatus(ResponseStatus.WARNING)
                .build());
    }
}