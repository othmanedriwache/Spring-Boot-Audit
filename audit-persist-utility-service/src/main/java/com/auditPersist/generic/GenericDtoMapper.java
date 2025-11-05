package com.auditPersist.generic;


import java.util.List;

public interface GenericDtoMapper<ENTITY, BASIC_DTO> {

    BASIC_DTO entityToBasicDto(ENTITY entity);

    List<BASIC_DTO> entitiesToBasicDtos(List<ENTITY> entities);

    ENTITY basicDtoToEntity(BASIC_DTO entityDto);

    List<ENTITY> basicDtosToEntities(List<BASIC_DTO> entitiesDto);

}
