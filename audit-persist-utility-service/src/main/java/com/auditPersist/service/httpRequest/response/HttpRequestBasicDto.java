package com.auditPersist.service.httpRequest.response;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class HttpRequestBasicDto {

     private String id;

     private String url;

     private String method;

     private String host;

     private int status;

     private String inputLine;

     private LocalDateTime inputDate;

     private String exceptionInput;

     private String outputLine;
     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
     private LocalDateTime outputDate;

     private String exceptionOutput;

     private Integer duration;

     private  String logPath;

     private ApplicationInstanceDto applicationInstance;

     private String returnContent;
}
