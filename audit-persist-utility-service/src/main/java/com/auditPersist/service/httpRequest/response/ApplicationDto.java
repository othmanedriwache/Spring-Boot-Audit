package com.auditPersist.service.httpRequest.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationDto {


    private String id;

    private String name;

    private String version;

    private LocalDateTime creationDate;

}
