package com.auditPersist.service.application.response;


import com.auditPersist.entity.Application;
import com.auditPersist.generic.GenericDtoMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApplicationDtoMapper extends GenericDtoMapper<Application,ApplicationDto> {
}
