package com.auditWriter.service.auditStrategy;

import com.auditWriter.model.LogInfo;

public interface AuditPersist {
    void save(LogInfo logInfo);
}
