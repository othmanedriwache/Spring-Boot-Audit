package com.auditPersist.service.application.response;

import com.auditPersist.entity.ApplicationInstance;
import com.auditPersist.generic.GenericDtoMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApplicationInstanceDtoMapper extends GenericDtoMapper<ApplicationInstance, ApplicationInstanceDto> {
}