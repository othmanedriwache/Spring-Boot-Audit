package com.auditPersist.service.httpRequest.response;

import com.auditPersist.model.FunctionType;
import com.auditPersist.model.ParentTreeStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FunctionRequestDto {

    private String id;

    private FunctionType type;

    private String path;

    private String packagePath;

    private String returnType;

    private String functionName;

    private String returnContent;

    private String inputLine;

    private LocalDateTime inputDate;

    private String exceptionInput;

    private String outputLine;

    private LocalDateTime outputDate;

    private String exceptionOutput;

    private Integer duration;

    private ParentTreeStatus parentTreeStatus;

    private FunctionRequestDto exception;

    private List<FunctionRequestArgumentDto> arguments;

    private List<BusinessRequestDto> businessRequests;

    private FunctionRequestParentDto parent;

    // this will be mapped manually
    private List<FunctionRequestDto> children;

}
