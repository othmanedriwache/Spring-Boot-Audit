
package com.auditPersist.batch.task.auditDatabase;

import com.auditPersist.model.LogInfo;
import com.auditPersist.model.LogReader;
import com.auditPersist.service.auditPersistService.AuditPersistService;
import com.auditPersist.utils.ApplicationInstanceManager;
import com.auditWriter.entity.LogDump;
import com.google.gson.Gson;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class AuditDatabaseProcessor implements ItemProcessor<LogDump, LogDump> {

    private final AuditPersistService auditPersistService;
    private final Gson gson;

    @Autowired
    public AuditDatabaseProcessor(@Lazy AuditPersistService auditPersistService) {
        this.auditPersistService = auditPersistService;
        this.gson = new Gson();
    }

    @Override
    public LogDump process(LogDump logDump) {
        try {
            LogReader logReader = buildLogReader(logDump);
            auditPersistService.save(logReader);
            logDump.setPersisted("TRUE");
        } catch (Exception e) {
            logDump.setException("TRUE");
        }
        return logDump;
    }

    private LogReader buildLogReader(LogDump logDump) {
        return LogReader.builder()
                .logInfo(parseContent(logDump.getContent()))
                .auditContent(logDump.getContent())
                .auditLine(logDump.getId())
                .auditLogFilePath("DATABASE")
                .auditTime(logDump.getTime())
                .applicationInstance(resolveApplicationInstance(logDump))
                .build();
    }

    private LogInfo parseContent(String content) {
        return gson.fromJson(content, LogInfo.class);
    }

    private com.auditPersist.entity.ApplicationInstance resolveApplicationInstance(LogDump logDump) {
        return ApplicationInstanceManager.getApplicationInstance(logDump.getApplicationInstance());
    }
}