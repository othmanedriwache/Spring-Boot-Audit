package com.auditPersist.service.application.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApplicationWithInstancesDto {

    private String id;

    private String name;

    private String version;

    private LocalDateTime creationDate;

    private List<ApplicationInstanceDto> applicationInstances;

}