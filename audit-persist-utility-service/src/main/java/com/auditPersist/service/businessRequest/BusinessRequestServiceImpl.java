package com.auditPersist.service.businessRequest;

import com.auditPersist.entity.BusinessRequest;
import com.auditPersist.entity.FunctionRequest;
import com.auditPersist.entity.HttpRequest;
import com.auditPersist.generic.GenericServiceImpl;
import com.auditPersist.model.BusinessRequestType;
import com.auditPersist.model.LogReader;
import com.auditPersist.model.ParentTreeStatus;
import com.auditPersist.repository.BusinessRequestRepository;
import com.auditPersist.service.functionRequest.FunctionRequestService;
import com.auditPersist.service.httpRequest.HttpRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessRequestServiceImpl extends GenericServiceImpl<BusinessRequest, BusinessRequestRepository, String> implements BusinessRequestService {

    @Autowired
    HttpRequestService httpRequestService;

    @Autowired
    FunctionRequestService functionRequestService;

    public BusinessRequestServiceImpl(BusinessRequestRepository entityRepository) {
        super(entityRepository);
    }

    @Override
    public BusinessRequest saveFromLogFileReader(LogReader logReader) {
        switch (logReader.getLogInfo().getLogInfoType()) {
            case BUSINESS_ERROR_LOG:
                this.createBusinessErrorLogFromLogFileReader(logReader, BusinessRequestType.ERROR);
                break;
            case BUSINESS_INFO_LOG:
                this.createBusinessErrorLogFromLogFileReader(logReader, BusinessRequestType.INFO);
                break;
        }
        return null;
    }

    @Override
    public void organizeBusinessTree() {
        List<BusinessRequest> businessRequests = this.getEntityRepository().findByParentTreeStatus(ParentTreeStatus.PENDING.name());
        if (businessRequests.isEmpty())
            return;
        businessRequests.forEach((businessRequest) -> {
            if(businessRequest.getHttpRequest() !=null) {
                FunctionRequest functionRequest = functionRequestService.findBusinessRequestRelatedFunctionRequest(businessRequest.getHttpRequest().getId(), businessRequest.getInputDate());
                businessRequest.setFunctionRequest(functionRequest);
            }
            businessRequest.setParentTreeStatus(ParentTreeStatus.FINISHED);

        });
    }


    private BusinessRequest createBusinessErrorLogFromLogFileReader(LogReader logReader, BusinessRequestType businessRequestType) {

        BusinessRequest businessRequest =
                BusinessRequest.builder()
                        .type(businessRequestType)
                        .inputDate(logReader.getAuditTime())
                        .inputLine(logReader.getAuditLine())
                        .parentTreeStatus(ParentTreeStatus.PENDING)
                .content(logReader.getLogInfo().getBusinessLog())
                .build();

        HttpRequest httpRequest = httpRequestService.findById(logReader.getLogInfo().getHttpRequestId());
        if (httpRequest == null) {
            businessRequest.setHttpRequest(null);
            businessRequest.setException("Http Request with id number : " + logReader.getLogInfo().getHttpRequestId() + " couldn't be found ");
        } else {
            businessRequest.setHttpRequest(httpRequest);
        }
        return this.save(businessRequest);
    }

}
