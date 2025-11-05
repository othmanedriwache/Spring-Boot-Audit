package com.auditWriter.interceptor;

import com.auditWriter.constant.AuditConstant;
import com.auditWriter.model.ApiDescription;
import com.auditWriter.model.LogInfo;
import com.auditWriter.model.LogInfoType;
import com.auditWriter.service.auditStrategy.AuditPersist;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class IDGeneratorInterceptor implements HandlerInterceptor {

    @Autowired
    private Gson gson;
    private final AuditPersist auditPersist;

    @Autowired
    public IDGeneratorInterceptor(AuditPersist auditPersist,Gson gson) {
        this.auditPersist = auditPersist;
        this.gson = gson;
    }

    @Override
    public boolean preHandle(HttpServletRequest requestServlet, HttpServletResponse responseServlet, Object handler){
        requestServlet.setAttribute(AuditConstant.PARENT_REQUEST_ID, requestServlet.getHeader(AuditConstant.UNIQUE_REQUEST_ID));
        requestServlet.setAttribute(AuditConstant.UNIQUE_REQUEST_ID, UUID.randomUUID().toString());
        try {
            Map<String, List<String>>  headersFromServlet= getHeadersFromServlet(requestServlet);
            if(!headersFromServlet.containsKey(AuditConstant.CORRELATIONID))
                headersFromServlet.put(AuditConstant.CORRELATIONID, Collections.singletonList(MDC.get(AuditConstant.CORRELATIONID)));
            LogInfo logInfo = LogInfo.builder()
                    .httpRequestId(getHttpServletAttributeOrNull(requestServlet, AuditConstant.UNIQUE_REQUEST_ID))
                    .httpParentId(getHttpServletAttributeOrNull(requestServlet, AuditConstant.PARENT_REQUEST_ID))
                    .logInfoType(LogInfoType.HTTP_REQUEST)
                    .apiDescription(
                            ApiDescription.builder()
                                    .host(requestServlet.getRemoteAddr())
                                    .method(requestServlet.getMethod())
                                    .parameters(requestServlet.getParameterMap())
                                    .headers(headersFromServlet)
                                    .url(requestServlet.getRequestURL().toString())
                                    .build()
                    )
                    .build();
            auditPersist.save(logInfo);
        } catch (Exception exception) {
            log.error(AuditConstant.FAILED_TO_LOG_HTTP_REQUEST +" "+ exception.getMessage());
        }
        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest requestServlet,
            HttpServletResponse responseServlet,
            Object handler, Exception ex) {
        try {
            LogInfo logInfo = LogInfo.builder()
                    .httpRequestId(requestServlet.getAttribute(AuditConstant.UNIQUE_REQUEST_ID).toString())
                    .httpRequestStatus(responseServlet.getStatus())
                    .logInfoType(LogInfoType.HTTP_RESPONSE)
                    .build();

            auditPersist.save(logInfo);
        } catch (Exception exception) {
            log.error(AuditConstant.FAILED_TO_LOG_HTTP_RESPONSE + exception.getMessage());
        }
    }

    private String getHttpServletAttributeOrNull(HttpServletRequest requestServlet,String attribute){
        String attributeValue;
        try{
            attributeValue = requestServlet.getAttribute(attribute).toString();
        }catch (Exception ignored){
            attributeValue = null;
        }
        return attributeValue;
    }

    private Map<String, List<String>> getHeadersFromServlet(HttpServletRequest requestServlet){
        return  Collections.list(requestServlet.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        h -> Collections.list(requestServlet.getHeaders(h))
                ));
    }

}


