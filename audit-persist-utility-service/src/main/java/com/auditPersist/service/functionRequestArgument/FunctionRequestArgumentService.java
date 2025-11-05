package com.auditPersist.service.functionRequestArgument;

import com.auditPersist.entity.FunctionRequest;
import com.auditPersist.entity.FunctionRequestArgument;
import com.auditPersist.generic.GenericService;

import java.util.List;

public interface FunctionRequestArgumentService  extends GenericService<FunctionRequestArgument, String> {

    List<FunctionRequestArgument> saveFromFunctionDescription(String[] argumentType, List<String>  argumentData, FunctionRequest functionRequest);

}

