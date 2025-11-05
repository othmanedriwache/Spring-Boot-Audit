
package com.auditPersist.batch.step;

import com.auditPersist.batch.task.auditDatabase.AuditDatabaseProcessor;
import com.auditPersist.batch.task.auditDatabase.AuditDatabaseReader;
import com.auditPersist.batch.task.auditDatabase.AuditDatabaseWriter;
import com.auditPersist.entity.ApplicationInstance;
import com.auditWriter.entity.LogDump;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuditDatabaseStep {

    @Value("${audit.chunkSize:10000}")
    private int chunkSize;

    private final StepBuilderFactory stepBuilderFactory;
    private final AuditDatabaseProcessor processor;
    private final AuditDatabaseWriter writer;
    private final AuditDatabaseReader reader;

    @Autowired
    public AuditDatabaseStep(StepBuilderFactory stepBuilderFactory,
                             AuditDatabaseProcessor processor,
                             AuditDatabaseWriter writer,
                             AuditDatabaseReader reader) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.processor = processor;
        this.writer = writer;
        this.reader = reader;
    }

    public Step auditChunkStep(ApplicationInstance applicationInstance) {
        return stepBuilderFactory.get("process-database-audit")
                .<LogDump, LogDump>chunk(chunkSize)
                .reader(reader.logDumpReader(applicationInstance))
                .processor(processor)
                .writer(writer)
                .build();
    }
}
