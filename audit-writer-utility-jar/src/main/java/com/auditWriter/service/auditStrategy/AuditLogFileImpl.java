package com.auditWriter.service.auditStrategy;

import com.auditWriter.constant.AuditConstant;
import com.auditWriter.model.LogInfo;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuditLogFileImpl implements AuditStrategy {

    @Autowired
    private Gson gson;

    @Value("${audit.async:false}")
    private String auditAsync;


    @Override
    public void saveLog(LogInfo logInfo) {
        if (auditAsync.equals("true"))
            saveLogAsync(logInfo);
        else
            saveLogSync(logInfo);
    }

    void saveLogSync(LogInfo logInfo) {
        log.info(AuditConstant.AUDIT_KEY_SEPARATOR + gson.toJson(logInfo));
    }

    @Async
    void saveLogAsync(LogInfo logInfo) {
        saveLogSync(logInfo);
    }
}
