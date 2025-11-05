package com.auditPersist.service.functionRequest;

import com.auditPersist.entity.FunctionRequest;
import com.auditPersist.entity.HttpRequest;
import com.auditPersist.exeptions.NoValidDataException;
import com.auditPersist.generic.GenericServiceImpl;
import com.auditPersist.model.FunctionDescription;
import com.auditPersist.model.FunctionType;
import com.auditPersist.model.LogReader;
import com.auditPersist.model.ParentTreeStatus;
import com.auditPersist.repository.FunctionRequestRepository;
import com.auditPersist.service.functionRequestArgument.FunctionRequestArgumentService;
import com.auditPersist.service.httpRequest.HttpRequestService;
import com.auditPersist.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.auditPersist.constant.AuditConstant.NO_AUDITABLE_CLASS_IN_EXCEPTION_HANDLER;

@Service
public class FunctionRequestServiceImpl extends GenericServiceImpl<FunctionRequest, FunctionRequestRepository, String> implements FunctionRequestService {

    @Autowired
    FunctionRequestArgumentService functionRequestArgumentService;

    @Autowired
    HttpRequestService httpRequestService;

    public FunctionRequestServiceImpl(FunctionRequestRepository entityRepository) {
        super(entityRepository);
    }

    @Override
    public FunctionRequest saveFromLogFileReader(LogReader logReader) {
        switch (logReader.getLogInfo().getLogInfoType()) {
            case FUNCTION_REQUEST:
                this.createFunctionRequestFromLogFileReader(logReader);
                break;
            case FUNCTION_RESPONSE:
                this.createFunctionResponseFromLogFileReader(logReader);
                break;
        }
        return null;
    }

    @Override
    public FunctionRequest findBusinessRequestRelatedFunctionRequest(String httpRequestsId, LocalDateTime inputDate) {
        List<FunctionRequest> parent = this.getEntityRepository().findBusinessRequestRelatedFunctionRequest(httpRequestsId, inputDate);
        if (!parent.isEmpty())
            return parent.get(0);
        return null;
    }

    @Override
    public void updateExceptionFunctionTree() {
        List<FunctionRequest> functionRequests = this.getEntityRepository().findFunctionRequestWhereOutputLineIsNull();
        if (functionRequests.isEmpty())
            return;
        functionRequests.forEach((functionRequest) -> {
            if (functionRequest.getHttpRequest() == null)
                return;
            if(functionRequest.getException() != null){
                functionRequest.setOutputDate(functionRequest.getException().getOutputDate());
                functionRequest.setOutputLine(functionRequest.getException().getOutputLine());
            }else{
                List<FunctionRequest> exceptionFunctionRequest = this.getEntityRepository().findFunctionRequestByHttpRequestsIdAndTypeAndTokenBefore(functionRequest.getHttpRequest().getId(), FunctionType.EXCEPTION.name());
                if (exceptionFunctionRequest.isEmpty()) {
                    functionRequest.setExceptionOutput(NO_AUDITABLE_CLASS_IN_EXCEPTION_HANDLER);
                    functionRequest.setOutputDate(functionRequest.getHttpRequest().getOutputDate());
                    functionRequest.setOutputLine(String.valueOf(0));
                }else{
                    functionRequest.setException(exceptionFunctionRequest.get(0));
                    functionRequest.setOutputDate(exceptionFunctionRequest.get(0).getOutputDate());
                    functionRequest.setOutputLine(exceptionFunctionRequest.get(0).getOutputLine());
                }
            }
            if(functionRequest.getInputDate() != null && functionRequest.getOutputDate() != null)
                functionRequest.setDuration(Duration.between(functionRequest.getInputDate(), functionRequest.getOutputDate()).getNano());
            this.save(functionRequest);
        });
    }

    @Override
    public void updateFunctionWhereExceptionEqualParent() {
        List<FunctionRequest> functionRequests = this.getEntityRepository().getFunctionRequestWhereParentIdEqualExceptionId();
        if (functionRequests.isEmpty())
            return;
        functionRequests.forEach((functionRequest) -> {
            functionRequest.setParent(null);
            this.save(functionRequest);
        });
    }

    @Override
    public void organizeFunctionTree() {
        List<FunctionRequest> functionRequests = this.getEntityRepository().findByParentTreeStatus(ParentTreeStatus.PENDING.name());
        if (functionRequests.isEmpty())
            return;
        functionRequests.forEach((functionRequest) -> {
            if (functionRequest.getHttpRequest() != null) {
                List<FunctionRequest> parent = this.getEntityRepository().findFunctionRequestParent(functionRequest.getHttpRequest().getId(), functionRequest.getInputDate(), functionRequest.getOutputDate(), functionRequest.getId());
                if (!parent.isEmpty())
                    functionRequest.setParent(parent.get(0));
            }
            functionRequest.setParentTreeStatus(ParentTreeStatus.FINISHED);
            this.save(functionRequest);
        });
    }

    @Override
    public void deleteAll(Iterable<FunctionRequest> functionRequests) {
        try {
            // With cascade=ALL configured on arguments relationship,
            // deleting FunctionRequest will automatically delete:
            // - FunctionRequestArguments
            this.getEntityRepository().deleteAll(functionRequests);
        } catch (Exception e) {
            throw new NoValidDataException("This Records Cant Be Deleted Contact Admin For More Information: " + e.getMessage());
        }
    }

    private FunctionRequest createFunctionRequestFromLogFileReader(LogReader logReader) {
        FunctionDescription functionDescription = logReader.getLogInfo().getFunctionDescription();
        FunctionRequest functionRequest =
                FunctionRequest.builder()
                        .id(functionDescription.getFunctionId())
                        .type(functionDescription.getFunctionType())
                        .path(functionDescription.getFunctionPath())
                        .inputDate(logReader.getAuditTime())
                        .inputLine(logReader.getAuditLine())
                        .parentTreeStatus(ParentTreeStatus.PENDING)
                        .build();
        HttpRequest httpRequest = httpRequestService.findById(logReader.getLogInfo().getHttpRequestId());
        if (httpRequest == null) {
            functionRequest.setHttpRequest(null);
            functionRequest.setExceptionInput("Http Request with id number : " + logReader.getLogInfo().getHttpRequestId() + " coudn t be found ");
        } else {
            functionRequest.setHttpRequest(httpRequest);
        }
        functionRequest = this.save(functionRequest);
        if(functionDescription.getExceptionSource() != null)
            updateFunctionRequestByExceptionID(functionRequest,functionDescription.getExceptionSource());
        functionRequest = saveFunctionArguments(functionDescription, functionRequest);
        return this.save(functionRequest);
    }

    private void updateFunctionRequestByExceptionID(FunctionRequest exceptionFunctionRequest , String exceptionSource){
        Optional<FunctionRequest> optionalFunctionRequestExceptionSource = this.getEntityRepository().findById(exceptionSource);
        if (!optionalFunctionRequestExceptionSource.isPresent())
            return;
        FunctionRequest functionRequestExceptionSource = optionalFunctionRequestExceptionSource.get();
        functionRequestExceptionSource.setException(exceptionFunctionRequest);
        this.save(functionRequestExceptionSource);
    }

    private FunctionRequest createFunctionResponseFromLogFileReader(LogReader logReader) {
        FunctionDescription functionDescription = logReader.getLogInfo().getFunctionDescription();
        FunctionRequest functionRequest = this.createFunctionRequestFromResponseIfNotFound(logReader);
        functionRequest.setOutputLine(logReader.getAuditLine());
        functionRequest.setOutputDate(logReader.getAuditTime());
        if(functionRequest.getInputDate() != null && functionRequest.getOutputDate() != null)
            functionRequest.setDuration(Duration.between(functionRequest.getInputDate(), functionRequest.getOutputDate()).getNano());
        functionRequest.setReturnContent(functionDescription.getFunctionResponse());
        return this.save(functionRequest);
    }

    public FunctionRequest saveFunctionArguments(FunctionDescription functionDescription, FunctionRequest functionRequest) {

        String functionDetails = functionDescription.getFunctionPath().startsWith("execution(") ? StringUtil.getFunctionContent(functionDescription.getFunctionPath()) : functionDescription.getFunctionPath();
        String[] functionDefinition = functionDetails.split(" ");
        String[] functionTypesDefinition;
        int i = functionDefinition.length - 1;
        int count = 0;
        while (i > 0 && count < 2) {
            if (i == functionDefinition.length - 1) {
                functionRequest.setFunctionName(getFunctionNameFromString(functionDefinition[i]));
                functionRequest.setPackagePath(getPackagePathFromString(functionDefinition[i]));
                functionTypesDefinition = StringUtil.getFunctionContent(functionDefinition[i]).split(" ");
                functionRequest.setArguments(functionRequestArgumentService.saveFromFunctionDescription(functionTypesDefinition, functionDescription.getFunctionArguments(), functionRequest));
            }
            if (i == functionDefinition.length - 2)
                functionRequest.setReturnType(functionDefinition[i]);
            i--;
            count++;
        }
        return functionRequest;
    }

    private String getFunctionNameFromString(String str) {
        String result;
        String[] functionName = new String[0];
        String[] functionFullPath = str.split("\\(");
        if (functionFullPath.length != 0)
            functionName = functionFullPath[0].split("\\.");
        result = functionName[functionName.length - 1];
        return result;
    }

    private String getPackagePathFromString(String str) {
        return str.split(getFunctionNameFromString(str))[0];
    }

    private FunctionRequest createFunctionRequestFromResponseIfNotFound(LogReader logReader) {
        FunctionDescription functionDescription = logReader.getLogInfo().getFunctionDescription();
        FunctionRequest functionRequest = this.findById(functionDescription.getFunctionId());
        HttpRequest httpRequest = httpRequestService.findById(logReader.getLogInfo().getHttpRequestId());
        if (functionRequest == null) {
            String errorMsg = "function input could not be found";
            functionRequest =
                    FunctionRequest.builder()
                            .id(functionDescription.getFunctionId())
                            .exceptionInput(errorMsg)
                            .type(functionDescription.getFunctionType())
                            .inputDate(logReader.getAuditTime())
                            .inputLine(logReader.getAuditLine())
                            .parentTreeStatus(ParentTreeStatus.FINISHED)
                            .build();
            if (httpRequest == null) {
                functionRequest.setHttpRequest(null);
                errorMsg += " , Http Request with id number : " + logReader.getLogInfo().getHttpRequestId() + " couldn't be found ";
                functionRequest.setExceptionInput(errorMsg);
            } else {
                functionRequest.setHttpRequest(httpRequest);
            }
            functionRequest = this.save(functionRequest);
        }
        return functionRequest;
    }

}