package com.auditWriter.service.auditStrategy;

import com.auditWriter.model.LogInfo;

public interface AuditStrategy {
    
    void saveLog(LogInfo logInfo);
}
