package com.auditPersist.service.httpRequest.response;

import lombok.Data;

@Data
public class HttpRequestParameterDto {

    private String id;

    private String parameter;

    private String content;
}
