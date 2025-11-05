package com.auditPersist.service.httpRequest.response;


import com.auditPersist.entity.HttpRequest;
import com.auditPersist.generic.GenericDtoMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HttpRequestMapper extends GenericDtoMapper<HttpRequest, HttpRequestDto> {

}
