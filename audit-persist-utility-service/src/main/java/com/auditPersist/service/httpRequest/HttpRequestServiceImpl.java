package com.auditPersist.service.httpRequest;

import com.auditPersist.entity.HttpRequest;
import com.auditPersist.entity.HttpRequestHeader;
import com.auditPersist.entity.HttpRequestParameter;
import com.auditPersist.exeptions.NoValidDataException;
import com.auditPersist.generic.GenericServiceImpl;
import com.auditPersist.model.ApiDescription;
import com.auditPersist.model.LogReader;
import com.auditPersist.repository.HttpRequestRepository;
import com.auditPersist.service.applicationInstance.ApplicationInstanceService;
import com.auditPersist.service.businessRequest.BusinessRequestService;
import com.auditPersist.service.functionRequest.FunctionRequestService;
import com.auditPersist.service.httpRequestHeader.HttpRequestHeaderService;
import com.auditPersist.service.httpRequestParameter.HttpRequestParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@Primary
public class HttpRequestServiceImpl  extends GenericServiceImpl<HttpRequest, HttpRequestRepository, String> implements HttpRequestService {

    @Autowired
    HttpRequestParameterService httpRequestParameterService;

    @Autowired
    HttpRequestHeaderService httpRequestHeaderService;

    @Autowired
    @Lazy
    FunctionRequestService functionRequestService;

    @Autowired
    @Lazy
    BusinessRequestService businessRequestService;

    @Autowired
    ApplicationInstanceService applicationInstanceService;

    public HttpRequestServiceImpl(HttpRequestRepository entityRepository) {
        super(entityRepository);
    }

    @Override
    public HttpRequest saveFromLogFileReader(LogReader logReader) {
        switch (logReader.getLogInfo().getLogInfoType()) {
            case HTTP_REQUEST:
                this.createHttpRequestFromLogFileReader(logReader);
                break;
            case HTTP_RESPONSE:
                this.createHttpResponseFromLogFileReader(logReader);
                break;
        }
        return null;
    }

    @Override
    public void deleteAll(Iterable<HttpRequest> httpRequests) {
        try {
            // With cascade=ALL configured on all relationships,
            // deleting HttpRequest will automatically delete:
            // - HttpRequestParameters
            // - HttpRequestHeaders
            // - FunctionRequests (and their Arguments and BusinessRequests)
            // - BusinessRequests
            this.getEntityRepository().deleteAll(httpRequests);
        } catch (Exception e) {
            throw new NoValidDataException("This Records Cant Be Deleted Contact Admin For More Information: " + e.getMessage());
        }
    }

    private HttpRequest createHttpRequestFromLogFileReader(LogReader logReader){
        ApiDescription apiDescription = logReader.getLogInfo().getApiDescription();
        HttpRequest httpRequestParent = this.findById(logReader.getLogInfo().getHttpParentId());
        HttpRequest httpRequest = HttpRequest.builder()
                .id(logReader.getLogInfo().getHttpRequestId())
                .applicationInstance(logReader.getApplicationInstance())
                .url(apiDescription.getUrl())
                .inputDate(logReader.getAuditTime())
                .inputLine(logReader.getAuditLine())
                .logPath(logReader.getAuditLogFilePath())
                .method(apiDescription.getMethod())
                .parent(httpRequestParent)
                .host(apiDescription.getHost())
                .build();
        httpRequest  = this.save(httpRequest);
        List<HttpRequestParameter> httpRequestParameters = httpRequestParameterService.saveFromMap(apiDescription.getParameters(), httpRequest);
        List<HttpRequestHeader> httpRequestHeaders = httpRequestHeaderService.saveFromMap(apiDescription.getHeaders(),httpRequest);
        httpRequest.setParameters(httpRequestParameters);
        httpRequest.setHeaders(httpRequestHeaders);
        return this.save(httpRequest);
    }

    private HttpRequest createHttpResponseFromLogFileReader(LogReader logReader){
        HttpRequest httpRequest =createHttpRequestFromResponseIfNotFound(logReader);
        httpRequest.setStatus(logReader.getLogInfo().getHttpRequestStatus());
        httpRequest.setOutputDate(logReader.getAuditTime());
        httpRequest.setOutputLine(logReader.getAuditLine());
        if(httpRequest.getInputDate() != null && httpRequest.getOutputDate() != null)
            httpRequest.setDuration(Duration.between(httpRequest.getInputDate(), httpRequest.getOutputDate()).getNano());
        return this.save(httpRequest);
    }

    private HttpRequest createHttpRequestFromResponseIfNotFound(LogReader logReader){
        HttpRequest httpRequest = this.findById(logReader.getLogInfo().getHttpRequestId());
        if(httpRequest == null) {
            String errorMsg = "httpRequest input could not be found";
            httpRequest = HttpRequest.builder()
                    .id(logReader.getLogInfo().getHttpRequestId())
                    .inputDate(logReader.getAuditTime())
                    .applicationInstance(logReader.getApplicationInstance())
                    .exceptionInput(errorMsg)
                    .build();
            this.save(httpRequest);
        }
        return httpRequest;
    }
}