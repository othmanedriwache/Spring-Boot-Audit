package com.auditPersist.service.httpRequest.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HttpRequestDto {

    private String id;

    private String url;

    private String method;

    private String host;

    private int status;

    private String inputLine;  // Changed from Integer to String to match entity

    private LocalDateTime inputDate;

    private String exceptionInput;

    private String outputLine;  // Changed from Integer to String to match entity

    private LocalDateTime outputDate;

    private String exceptionOutput;

    private Integer duration;

    private String logPath;

    private List<HttpRequestParameterDto> parameters;

    private List<HttpRequestHeaderDto> headers;

    private List<FunctionRequestDto> functionRequests;

    private List<BusinessRequestDto> businessRequests;

    private ApplicationInstanceDto applicationInstance;

    private String returnContent;
}