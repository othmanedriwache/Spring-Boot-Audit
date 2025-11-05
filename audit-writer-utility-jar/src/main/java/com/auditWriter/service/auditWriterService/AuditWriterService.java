package com.auditWriter.service.auditWriterService;

import com.auditWriter.annotations.AuditLogType;
import com.auditWriter.model.FunctionType;
import org.aspectj.lang.ProceedingJoinPoint;

public interface AuditWriterService {

    void logFunctionRequest(ProceedingJoinPoint joinPoint, FunctionType functionType, String functionId, String exceptionSource, AuditLogType auditLogType);

    void logFunctionResponse(Object response, FunctionType functionType, String functionId, AuditLogType auditLogType);

    void logBusinessInfo(String content);

    void logBusinessError(String content);
}
