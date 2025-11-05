package com.auditPersist.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FunctionDescription {

    private String functionId;
    private FunctionType functionType;
    private FunctionStatus functionStatus;
    private List<String> functionArguments;
    private String functionPath;
    private String functionResponse;
    private String exceptionSource;
}
