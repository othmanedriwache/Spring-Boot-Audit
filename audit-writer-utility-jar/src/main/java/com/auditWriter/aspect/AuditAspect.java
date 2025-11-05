package com.auditWriter.aspect;

import com.auditWriter.annotations.AuditLogType;
import com.auditWriter.annotations.AuditableFunction;
import com.auditWriter.constant.AuditConstant;
import com.auditWriter.model.FunctionType;
import com.auditWriter.service.auditWriterService.AuditWriterService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Aspect
@Component
@Slf4j
public class AuditAspect {

    @Autowired
    AuditWriterService auditWriterService;
    @Value("${audit.repository:false}")
    private boolean isRepositoryAuditable;
    @Value("${audit.controller:false}")
    private boolean isControllerAuditable;
    @Value("${audit.exception:false}")
    private boolean isExceptionAuditable;
    @Value("${audit.component:false}")
    private boolean isComponentAuditable;
    @Value("${audit.service:false}")
    private boolean isServiceAuditable;

    List<String> ignoredExceptions = Arrays.asList("class java.lang.reflect.UndeclaredThrowableException","");

    @Around("@annotation(com.auditWriter.annotations.AuditableFunction)")
    public Object logExecutionFunctionLevel(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        AuditableFunction myAnnotation = method.getAnnotation(AuditableFunction.class);
        AuditLogType auditLogType = myAnnotation.value();
        return this.processAroundJob(joinPoint,auditLogType);
    }

    @Around("@within(com.auditWriter.annotations.AuditableClass) && ! @annotation(com.auditWriter.annotations.NotAuditableFunction) && ! @annotation(com.auditWriter.annotations.AuditableFunction)")
    public Object logExecutionClassLevel(ProceedingJoinPoint joinPoint) throws Throwable {
        return this.processAroundJob(joinPoint,AuditLogType.SAVE_ALL);
    }

    public Object processAroundJob(ProceedingJoinPoint joinPoint,AuditLogType auditLogType) throws Throwable {
        Object response = null;
        FunctionType functionType = this.getFunctionTypeFromJoinPointAnnotation(joinPoint);
        boolean isAuditable= getAuditPermissions(functionType);
        String functionId = UUID.randomUUID().toString();
        if(isAuditable){
            logFunctionRequestDetails(joinPoint, functionType, functionId,null,auditLogType);
        }
        try{
            response =  joinPoint.proceed();
        }catch (Exception e){
            logInternalExceptionDetails(e,functionId,auditLogType);
            throw new Exception(e);
        }

        if(isAuditable){
            logFunctionResponseDetails(response, functionType, functionId,auditLogType);
        }
        return response;
    }

    private FunctionType getFunctionTypeFromJoinPointAnnotation(ProceedingJoinPoint joinPoint){
        try{
            Signature signature = joinPoint.getSignature();
            Annotation[] annotations  = signature.getDeclaringType().getDeclaredAnnotations();
            return getFunctionTypeFromAnnotationList(annotations);
        }catch (Exception e){
            return FunctionType.COMPONENT;
        }
    }

    private FunctionType getFunctionTypeFromAnnotationList(Annotation[] annotations){
        for (Annotation annotation : annotations) {
            if(annotation instanceof Service){
                return FunctionType.SERVICE;
            }
            if(annotation instanceof Repository){
                return FunctionType.REPOSITORY;
            }
            if(annotation instanceof Controller || annotation instanceof RestController){
                return FunctionType.CONTROLLER;
            }
            if(annotation instanceof ControllerAdvice){
                return FunctionType.EXCEPTION;
            }
            if(annotation instanceof Component){
                return FunctionType.COMPONENT;
            }
        }
        return FunctionType.COMPONENT;
    }

    private boolean getAuditPermissions(FunctionType functionType){
        boolean result = false;
        switch (functionType){
            case CONTROLLER:
                result = isControllerAuditable;
                break;
            case SERVICE:
                result = isServiceAuditable;
                break;
            case EXCEPTION:
                result = isExceptionAuditable;
                break;
            case REPOSITORY:
                result = isRepositoryAuditable;
                break;
            case COMPONENT:
                result = isComponentAuditable;
                break;
        }
        return result;
    }

    private void logInternalExceptionDetails(Exception exception, String functionIdParent,AuditLogType auditLogType ){
        FunctionType functionType = FunctionType.EXCEPTION;
        String functionId = UUID.randomUUID().toString();
        boolean isAuditable= getAuditPermissions(functionType);
        if(!isAuditable)
            return;
        Object response = AuditConstant.INTERNAL_EXCEPTION + " // EXCEPTION TYPE : " + exception.getClass() + "// EXCEPTION MESSAGE " + exception.getMessage();
        ProceedingJoinPoint joinPoint = new InternalExceptionJoinPoint(exception);
        if(isIgnoredExceptions(joinPoint))
            return;
        logFunctionRequestDetails(joinPoint, functionType, functionId,functionIdParent,auditLogType);
        logFunctionResponseDetails(response, functionType, functionId,auditLogType);
    }

    private void logFunctionRequestDetails(ProceedingJoinPoint joinPoint, FunctionType functionType, String functionId,String exceptionSource,AuditLogType auditLogType) {
        try {
            auditWriterService.logFunctionRequest(joinPoint, functionType, functionId,exceptionSource,auditLogType);
        } catch (Exception exception) {
            log.error(AuditConstant.FAILED_TO_LOG_FUNCTION_REQUEST + "{}", exception.getMessage());
        }

    }

    private void logFunctionResponseDetails(Object response, FunctionType functionType, String functionId,AuditLogType auditLogType) {
        try {
            auditWriterService.logFunctionResponse(response, functionType, functionId , auditLogType);
        } catch (Exception exception) {
            log.error(AuditConstant.FAILED_TO_LOG_FUNCTION_RESPONSE + "{}", exception.getMessage());
        }
    }

    public boolean isIgnoredExceptions(ProceedingJoinPoint joinPoint){
        return ignoredExceptions.contains(joinPoint.toLongString());
    }
}