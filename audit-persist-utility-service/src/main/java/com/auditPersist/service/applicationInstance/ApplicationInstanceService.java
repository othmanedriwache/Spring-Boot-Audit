package com.auditPersist.service.applicationInstance;

import com.auditPersist.entity.Application;
import com.auditPersist.entity.ApplicationInstance;
import com.auditPersist.generic.GenericService;
import com.auditWriter.entity.LogDump;

public interface ApplicationInstanceService extends GenericService<ApplicationInstance, String> {

    String getCurrentApplicationInstanceByHttpRequestId(String httpRequestId);

    ApplicationInstance getCurrentApplicationInstanceByLogDumpInstance(LogDump logDumpInstance);

    String findApplicationInstanceCurrentLine(ApplicationInstance applicationInstance);

    void cleanApplicationInstanceLogsById(String id);

    void cleanApplicationInstance(ApplicationInstance applicationInstance);

    ApplicationInstance createNewInstance(Application application);
}
