
package com.auditPersist.batch.task.auditDatabase;

import com.auditPersist.entity.ApplicationInstance;
import com.auditWriter.entity.LogDump;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuditDatabaseReader {

    private static final String QUERY = "SELECT l FROM LogDump l " +
            "WHERE l.applicationInstance = :applicationInstance " +
            "AND l.exception IS NULL " +
            "AND l.persisted IS NULL " +
            "ORDER BY l.time ASC";

    @Value("${audit.chunkSize:10000}")
    private int chunkSize;

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public AuditDatabaseReader(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public JpaPagingItemReader<LogDump> logDumpReader(ApplicationInstance applicationInstance) {
        return new JpaPagingItemReaderBuilder<LogDump>()
                .name("logDumpReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString(QUERY)
                .parameterValues(buildParameterMap(applicationInstance))
                .pageSize(chunkSize)
                .build();
    }

    private Map<String, Object> buildParameterMap(ApplicationInstance applicationInstance) {
        Map<String, Object> params = new HashMap<>();
        params.put("applicationInstance", applicationInstance.getId());
        return params;
    }
}