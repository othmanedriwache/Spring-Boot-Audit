package com.auditPersist.batch.job;

import com.auditWriter.entity.LogDump;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LogDumpWriter implements ItemWriter<LogDump> {

    @Override
    public void write(List<? extends LogDump> items) throws Exception {
    }
}