
package com.auditPersist.batch.task.audit;

import com.auditPersist.batch.task.LineMapperInfo;
import com.auditPersist.entity.ApplicationInstance;
import com.auditPersist.model.LogReader;
import com.auditPersist.service.applicationInstance.ApplicationInstanceService;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class AuditReader {

    private final ApplicationInstanceService applicationInstanceService;
    private final LineMapperInfo lineMapperInfo;

    @Autowired
    public AuditReader(ApplicationInstanceService applicationInstanceService,
                       LineMapperInfo lineMapperInfo) {
        this.applicationInstanceService = applicationInstanceService;
        this.lineMapperInfo = lineMapperInfo;
    }

    public FlatFileItemReader<LogReader> flatFileItemReader(ApplicationInstance applicationInstance,
                                                            Resource logFile) {
        FlatFileItemReader<LogReader> reader = new FlatFileItemReader<>();
        reader.setResource(logFile);
        reader.setLinesToSkip(determineLinesToSkip(applicationInstance));
        reader.setLineMapper(lineMapperInfo.lineMapper(applicationInstance));
        return reader;
    }

    private int determineLinesToSkip(ApplicationInstance applicationInstance) {
        String currentLine = applicationInstanceService.findApplicationInstanceCurrentLine(applicationInstance);
        return Integer.parseInt(currentLine);
    }
}