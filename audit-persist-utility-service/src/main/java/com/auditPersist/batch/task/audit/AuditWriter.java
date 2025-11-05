
package com.auditPersist.batch.task.audit;

import com.auditPersist.model.LogReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuditWriter implements ItemWriter<LogReader> {

    @Override
    public void write(List<? extends LogReader> items) {
    }
}