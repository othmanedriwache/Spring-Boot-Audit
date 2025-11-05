package com.auditWriter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LogInfo {

    private String httpParentId;

    private String httpRequestId;

    private int httpRequestStatus = 200;

    private LogInfoType logInfoType;

    private FunctionDescription functionDescription;

    private ApiDescription apiDescription;

    private String businessLog ;

    @Builder.Default
    private LocalDateTime logTime = LocalDateTime.now();

}
