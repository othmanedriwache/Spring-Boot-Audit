package com.auditPersist.batch.job;

import com.auditWriter.entity.LogDump;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class LogDumpProcessor implements ItemProcessor<LogDump, LogDump> {

    @Override
    public LogDump process(LogDump logDump) throws Exception {
        return logDump;
    }
}