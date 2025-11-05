package com.auditPersist.service.auditPersistService;

import com.auditPersist.model.LogReader;
import com.auditPersist.service.businessRequest.BusinessRequestService;
import com.auditPersist.service.functionRequest.FunctionRequestService;
import com.auditPersist.service.httpRequest.HttpRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuditPersistServiceImpl implements AuditPersistService {

    @Autowired
    FunctionRequestService functionRequestService;

    @Autowired
    HttpRequestService httpRequestService;

    @Autowired
    BusinessRequestService businessRequestService;

    @Override
    public void save(LogReader logReader) {
        switch (logReader.getLogInfo().getLogInfoType()){
            case HTTP_REQUEST:
            case HTTP_RESPONSE:
                httpRequestService.saveFromLogFileReader(logReader);
                break;
            case FUNCTION_REQUEST:
            case FUNCTION_RESPONSE:
                functionRequestService.saveFromLogFileReader(logReader);
                break;
            case BUSINESS_ERROR_LOG:
            case BUSINESS_INFO_LOG:
                businessRequestService.saveFromLogFileReader(logReader);
                break;
        }
    }

    @Override
    public void organizeAuditTree() {
        functionRequestService.updateExceptionFunctionTree();
        functionRequestService.organizeFunctionTree();
        functionRequestService.updateFunctionWhereExceptionEqualParent();
        businessRequestService.organizeBusinessTree();

    }
}