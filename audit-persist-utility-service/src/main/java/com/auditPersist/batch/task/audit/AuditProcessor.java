
package com.auditPersist.batch.task.audit;

import com.google.gson.Gson;
import com.auditPersist.constant.AuditConstant;
import com.auditPersist.model.LogInfo;
import com.auditPersist.model.LogReader;
import com.auditPersist.service.auditPersistService.AuditPersistService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class AuditProcessor implements ItemProcessor<LogReader, LogReader> {

    private final AuditPersistService auditPersistService;
    private final Gson gson;

    @Autowired
    public AuditProcessor(@Lazy AuditPersistService auditPersistService) {
        this.auditPersistService = auditPersistService;
        this.gson = new Gson();
    }

    @Override
    public LogReader process(LogReader logReader) {
        if (shouldSkipLog(logReader)) {
            return null;
        }

        LogInfo logInfo = parseLogContent(logReader.getAuditContent());
        if (logInfo == null) {
            return null;
        }

        logReader.setLogInfo(logInfo);
        auditPersistService.save(logReader);

        return logReader;
    }

    private boolean shouldSkipLog(LogReader logReader) {
        return logReader.getAuditContent() == null ||
                logReader.getAuditContent().equals(AuditConstant.AUDIT_IGNORE);
    }

    private LogInfo parseLogContent(String content) {
        try {
            return gson.fromJson(content, LogInfo.class);
        } catch (Exception e) {
            return null;
        }
    }
}