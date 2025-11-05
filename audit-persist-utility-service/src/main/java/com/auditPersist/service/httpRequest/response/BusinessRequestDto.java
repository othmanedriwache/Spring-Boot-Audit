package com.auditPersist.service.httpRequest.response;

import com.auditPersist.model.BusinessRequestType;
import com.auditPersist.model.ParentTreeStatus;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Data
public class BusinessRequestDto {

    private String id;

    private BusinessRequestType type;

    private String content;

    private String exception;

    private String inputLine;

    private LocalDateTime inputDate;

    @Enumerated(EnumType.STRING)
    private ParentTreeStatus parentTreeStatus;
}
