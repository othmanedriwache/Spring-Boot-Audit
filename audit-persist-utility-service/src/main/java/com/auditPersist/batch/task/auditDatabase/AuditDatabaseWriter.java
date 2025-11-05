
package com.auditPersist.batch.task.auditDatabase;

import com.auditWriter.entity.LogDump;
import com.auditWriter.service.logDumpService.LogDumpService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuditDatabaseWriter implements ItemWriter<LogDump> {

    private final LogDumpService logDumpService;

    @Autowired
    public AuditDatabaseWriter(LogDumpService logDumpService) {
        this.logDumpService = logDumpService;
    }

    @Override
    public void write(List<? extends LogDump> items) {
        logDumpService.saveLogDump((List<LogDump>) items);
    }
}