package com.auditPersist.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ApiDescription {
    private String url;
    private String method;
    private String host;
    private Map<String, String[]> parameters;
    private Map<String, List<String>> headers;
}