package com.auditPersist.service.application.dto;

import com.auditPersist.entity.Application;
import com.auditPersist.repository.ApplicationRepository;
import com.auditPersist.service.application.ApplicationServiceImpl;
import com.auditPersist.service.application.response.ApplicationDto;
import com.auditPersist.service.application.response.ApplicationDtoMapper;
import com.auditPersist.service.application.response.ApplicationWithInstancesDto;
import com.auditPersist.service.application.response.ApplicationWithInstancesDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceDtoImpl extends ApplicationServiceImpl implements ApplicationServiceDto   {

    @Autowired
    ApplicationDtoMapper applicationDtoMapper;

    @Autowired
    ApplicationWithInstancesDtoMapper applicationWithInstancesDtoMapper;

    public ApplicationServiceDtoImpl(ApplicationRepository entityRepository) {
        super(entityRepository);
    }

    @Override
    public ApplicationDto createIfNotExistDto(Application application) {
        return applicationDtoMapper.entityToBasicDto(this.createIfNotExist(application));
    }

    @Override
    public List<ApplicationWithInstancesDto> getAllApplicationsWithInstances() {
        List<Application> applications = this.findAll();
        return applications.stream()
                .map(app -> applicationWithInstancesDtoMapper.entityToBasicDto(app))
                .collect(Collectors.toList());
    }
}