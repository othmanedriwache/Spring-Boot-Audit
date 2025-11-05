
package com.auditPersist.batch.step;

import com.auditPersist.batch.task.audit.AuditProcessor;
import com.auditPersist.batch.task.audit.AuditReader;
import com.auditPersist.batch.task.audit.AuditWriter;
import com.auditPersist.entity.ApplicationInstance;
import com.auditPersist.model.LogReader;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class AuditStep {

    @Value("${audit.chunkSize:10000}")
    private int chunkSize;

    private final StepBuilderFactory stepBuilders;
    private final AuditReader reader;
    private final AuditWriter writer;
    private final AuditProcessor processor;

    @Autowired
    public AuditStep(StepBuilderFactory stepBuilders,
                     AuditReader reader,
                     AuditWriter writer,
                     AuditProcessor processor) {
        this.stepBuilders = stepBuilders;
        this.reader = reader;
        this.writer = writer;
        this.processor = processor;
    }

    public Step auditChunkStep(ApplicationInstance applicationInstance, Resource logFile) {
        return stepBuilders.get("process-audit-logs")
                .<LogReader, LogReader>chunk(chunkSize)
                .reader(reader.flatFileItemReader(applicationInstance, logFile))
                .processor(processor)
                .writer(writer)
                .build();
    }
}