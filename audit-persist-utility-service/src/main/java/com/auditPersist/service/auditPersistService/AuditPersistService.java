package com.auditPersist.service.auditPersistService;

import com.auditPersist.model.LogReader;

public interface AuditPersistService {

    void save(LogReader logReader);

    void organizeAuditTree();

}
