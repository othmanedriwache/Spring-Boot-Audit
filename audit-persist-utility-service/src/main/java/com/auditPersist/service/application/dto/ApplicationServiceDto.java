package com.auditPersist.service.application.dto;

import com.auditPersist.entity.Application;
import com.auditPersist.service.application.ApplicationService;
import com.auditPersist.service.application.response.ApplicationDto;
import com.auditPersist.service.application.response.ApplicationWithInstancesDto;

import java.util.List;

public interface ApplicationServiceDto extends ApplicationService {

    ApplicationDto createIfNotExistDto(Application application);

    List<ApplicationWithInstancesDto> getAllApplicationsWithInstances();


}
