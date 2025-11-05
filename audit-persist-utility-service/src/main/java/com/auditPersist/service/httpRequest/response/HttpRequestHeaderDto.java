package com.auditPersist.service.httpRequest.response;

import lombok.Data;

@Data
public class HttpRequestHeaderDto {

    private String id;

    private String header;

    private String content;
}
