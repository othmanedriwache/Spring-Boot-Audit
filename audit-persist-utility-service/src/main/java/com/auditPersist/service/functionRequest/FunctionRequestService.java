package com.auditPersist.service.functionRequest;

import com.auditPersist.entity.FunctionRequest;
import com.auditPersist.generic.GenericService;
import com.auditPersist.model.LogReader;

import java.time.LocalDateTime;

public interface FunctionRequestService extends GenericService<FunctionRequest, String> {

    FunctionRequest saveFromLogFileReader(LogReader logReader);

    FunctionRequest findBusinessRequestRelatedFunctionRequest(String httpRequestsId, LocalDateTime inputDate);

    void updateExceptionFunctionTree();

    void updateFunctionWhereExceptionEqualParent();

    void organizeFunctionTree();

}

