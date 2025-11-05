package com.auditWriter.service.auditWriterService;

import com.auditWriter.annotations.AuditLogType;
import com.auditWriter.constant.AuditConstant;
import com.auditWriter.model.*;
import com.auditWriter.service.auditStrategy.AuditPersist;
import com.auditWriter.utils.StringUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.auditWriter.constant.AuditConstant.NOT_AUDITABLE_REQUEST_PARAMETERS;
import static com.auditWriter.constant.AuditConstant.NOT_AUDITABLE_RESPONSE_RESULT;

@Service
@Slf4j
public class AuditWriterServiceImpl implements AuditWriterService {

    @Autowired
    private Gson gson;
    @Autowired
    HttpServletRequest request;

    @Autowired
    AuditPersist auditPersist;

    @Override
    public void logFunctionRequest(ProceedingJoinPoint joinPoint, FunctionType functionType, String functionId, String exceptionSource, AuditLogType auditLogType) {
        LogInfo logInfo = LogInfo.builder()
                .httpRequestId(request.getAttribute(AuditConstant.UNIQUE_REQUEST_ID).toString())
                .logInfoType(LogInfoType.FUNCTION_REQUEST)
                .functionDescription(
                        FunctionDescription.builder()
                                .functionType(functionType)
                                .functionArguments(getFunctionArgument(joinPoint,auditLogType))
                                .functionPath(joinPoint.toLongString())
                                .functionStatus(FunctionStatus.INPUT)
                                .functionId(functionId)
                                .exceptionSource(exceptionSource)
                                .build()
                )
                .build();
        auditPersist.save(logInfo);
    }

    private List<String> getFunctionArgument(ProceedingJoinPoint joinPoint , AuditLogType auditLogType){
        if(auditLogType != AuditLogType.SAVE_ALL && auditLogType != AuditLogType.SAVE_REQUEST){
            List<String> result = new ArrayList<>();
            result.add(NOT_AUDITABLE_REQUEST_PARAMETERS);
            return result;
        }

        return StringUtil.convertListOfObjectsToListOfStrings(joinPoint.getArgs());
    }

    @Override
    public void logFunctionResponse(Object response, FunctionType functionType, String functionId,AuditLogType auditLogType) {
        LogInfo logInfo = LogInfo.builder()
                .httpRequestId(request.getAttribute(AuditConstant.UNIQUE_REQUEST_ID).toString())
                .logInfoType(LogInfoType.FUNCTION_RESPONSE)
                .functionDescription(
                        FunctionDescription.builder()
                                .functionType(functionType)
                                .functionStatus(FunctionStatus.OUTPUT)
                                .functionId(functionId)
                                .functionResponse(getFunctionResponse(response,auditLogType))
                                .build()
                )
                .build();
        auditPersist.save(logInfo);
    }

    private String getFunctionResponse(Object response , AuditLogType auditLogType){
        if(auditLogType != AuditLogType.SAVE_ALL && auditLogType != AuditLogType.SAVE_RESPONSE){
            return NOT_AUDITABLE_RESPONSE_RESULT;
        }
        return Objects.toString(response, null);
    }

    @Override
    public void logBusinessInfo(String content) {
        buildBusinessRequest(content, LogInfoType.BUSINESS_INFO_LOG);
    }

    @Override
    public void logBusinessError(String content) {
        buildBusinessRequest(content, LogInfoType.BUSINESS_ERROR_LOG);
    }

    private void buildBusinessRequest(String content, LogInfoType logInfoType) {
        LogInfo logInfo = LogInfo.builder()
                .httpRequestId(request.getAttribute(AuditConstant.UNIQUE_REQUEST_ID).toString())
                .logInfoType(logInfoType)
                .businessLog(content)
                .build();
        auditPersist.save(logInfo);
    }
}
