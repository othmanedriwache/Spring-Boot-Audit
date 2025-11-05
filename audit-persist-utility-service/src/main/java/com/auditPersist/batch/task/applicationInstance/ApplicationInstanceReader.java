package com.auditPersist.batch.task.applicationInstance;

import com.auditPersist.batch.task.LineMapperInfo;
import com.auditPersist.model.LogReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInstanceReader {

    private final LineMapperInfo lineMapperInfo;

    @Autowired
    public ApplicationInstanceReader(LineMapperInfo lineMapperInfo) {
        this.lineMapperInfo = lineMapperInfo;
    }

    public FlatFileItemReader<LogReader> flatFileItemReader(Resource logFile) {
        FlatFileItemReader<LogReader> reader = new FlatFileItemReader<>();
        reader.setResource(logFile);
        reader.setLinesToSkip(0);
        reader.setLineMapper(lineMapperInfo.lineMapper(null));
        return reader;
    }
}