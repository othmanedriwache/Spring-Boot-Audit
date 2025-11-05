package com.auditPersist.batch.step;

import com.auditPersist.batch.task.applicationInstance.ApplicationInstanceProcessor;
import com.auditPersist.batch.task.applicationInstance.ApplicationInstanceReader;
import com.auditPersist.batch.task.applicationInstance.ApplicationInstanceWriter;
import com.auditPersist.model.LogReader;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInstanceStep {

    @Value("${audit.chunkSize:10000}")
    private int chunkSize;

    private final StepBuilderFactory stepBuilders;
    private final ApplicationInstanceReader reader;
    private final ApplicationInstanceWriter writer;
    private final ApplicationInstanceProcessor processor;

    @Autowired
    public ApplicationInstanceStep(StepBuilderFactory stepBuilders,
                                   ApplicationInstanceReader reader,
                                   ApplicationInstanceWriter writer,
                                   ApplicationInstanceProcessor processor) {
        this.stepBuilders = stepBuilders;
        this.reader = reader;
        this.writer = writer;
        this.processor = processor;
    }

    public Step applicationInstanceChunkStep(Resource logFile) {
        return stepBuilders.get("resolve-application-instance")
                .<LogReader, LogReader>chunk(chunkSize)
                .reader(reader.flatFileItemReader(logFile))
                .processor(processor)
                .writer(writer)
                .build();
    }
}
