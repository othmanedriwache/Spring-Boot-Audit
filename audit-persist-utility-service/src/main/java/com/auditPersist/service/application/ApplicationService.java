package com.auditPersist.service.application;

import com.auditPersist.entity.Application;
import com.auditPersist.generic.GenericService;
import org.springframework.web.multipart.MultipartFile;

public interface ApplicationService  extends GenericService<Application, String> {

    Application createIfNotExist(Application application);

    Application findByNameAndVersion(String applicationName, String applicationVersion);

    void cleanApplication(Application application);

    void cleanApplicationLogsById(String id);

    void cleanApplicationLogs(Application application);

    void cleanApplicationLogsByNameAndVersion(String applicationName, String applicationVersion);

    boolean isValidApplicationNameAndVersion(String applicationName, String applicationVersion);

    boolean isValidApplicationLogFile(MultipartFile logFile);

    void isValidApplicationElseThrow(Application application,MultipartFile logFile);

}
