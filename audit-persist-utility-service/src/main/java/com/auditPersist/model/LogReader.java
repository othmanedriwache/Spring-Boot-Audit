package com.auditPersist.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.auditPersist.entity.ApplicationInstance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class LogReader {

    private LogInfo logInfo;
    private String auditContent;
    private String auditLine;
    private String auditLogFilePath;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime auditTime;
    private ApplicationInstance applicationInstance;

}
