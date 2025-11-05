package com.auditPersist.service.httpRequest.response;

import lombok.Data;

@Data
public class FunctionRequestParentDto {

    private String id;

    private FunctionRequestParentDto parent;
}
