package com.auditWriter.service.auditStrategy;

import com.auditWriter.model.LogInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuditPersistImpl implements AuditPersist {

    @Autowired
    AuditLogFileImpl auditLogFile;
    @Autowired
    AuditDatabaseImpl auditDatabase;

    @Value("${audit.strategy:logFile}")
    private String auditStrategy;

    @Value("${audit.sync:true}")
    private String auditSync;

    public void save(LogInfo logInfo){
        if(Objects.equals(auditStrategy, "database")){
            auditDatabase.saveLog(logInfo);
        }
        else if(Objects.equals(auditStrategy, "logFile")){
            auditLogFile.saveLog(logInfo);
        }
    }

}
