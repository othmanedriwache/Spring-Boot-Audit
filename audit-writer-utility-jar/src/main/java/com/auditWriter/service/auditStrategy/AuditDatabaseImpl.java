package com.auditWriter.service.auditStrategy;

import com.auditWriter.entity.LogDump;
import com.auditWriter.model.LogInfo;
import com.auditWriter.repository.LogDumpRepository;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class AuditDatabaseImpl implements AuditStrategy {

    @Autowired
    LogDumpRepository logDumpRepository;

    @Autowired
    private Gson gson;

    @Value("${audit.async:false}")
    private String auditAsync;

    @Value("${audit.application-name:DEFAULT_APPLICATION_NAME}")
    private String applicationName;

    @Value("${audit.application-version:DEFAULT_APPLICATION_VERSION}")
    private String applicationVersion;

    private static final String applicationInstance = UUID.randomUUID().toString();


    @Override
    public void saveLog(LogInfo logInfo) {
        if (auditAsync.equals("true"))
            saveLogAsync(logInfo);
        else
            saveLogSync(logInfo);
    }

    void saveLogSync(LogInfo logInfo) {
        LogDump logDump = LogDump.builder()
                .time(LocalDateTime.now())
                .content(gson.toJson(logInfo))
                .applicationInstance(applicationInstance)
                .applicationName(applicationName)
                .applicationVersion(applicationVersion)
                .build();
        logDumpRepository.save(logDump);
    }

    @Async
    void saveLogAsync(LogInfo logInfo) {
        saveLogSync(logInfo);
    }
}
