package com.auditPersist.service.application.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationInstanceDto {

    private String id;

    private LocalDateTime creationDate;

}