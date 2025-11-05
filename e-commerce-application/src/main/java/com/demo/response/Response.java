package com.demo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * The type Response.
 */
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    /**
     * The Time stamp.
     */
    protected LocalDateTime timeStamp;
    /**
     * The Status code.
     */
    protected int statusCode;
    /**
     * The Status.
     */
    protected HttpStatus status;
    /**
     * The Developer status.
     */
    protected ResponseStatus developerStatus;
    /**
     * The Reason.
     */
    protected String reason;
    /**
     * The Message.
     */
    protected String message;
    /**
     * The Data.
     */
    protected Object data;


}
