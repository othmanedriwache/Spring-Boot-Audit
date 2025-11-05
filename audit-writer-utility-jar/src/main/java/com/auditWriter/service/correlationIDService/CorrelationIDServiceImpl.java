package com.auditWriter.service.correlationIDService;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class CorrelationIDServiceImpl implements CorrelationIDService{

    private static final String CORRELATIONID = "CorrelationId";

    @Override
    public void getCorrelationId(String correlationId) {
        if (correlationId != null && !correlationId.isEmpty()) {
            MDC.put(CORRELATIONID, correlationId);
            log.info("Correlation Id is Found in Headers: {}", MDC.get(CORRELATIONID));
        } else {
            MDC.put(CORRELATIONID, UUID.randomUUID().toString());
            log.info("No Correlation Id is found in Headers.Generated correlationId is: {}", MDC.get(CORRELATIONID));
        }

    }
}
