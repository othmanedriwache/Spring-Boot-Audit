package com.auditPersist.generic;


import java.util.List;

public interface GenericService<ENTITY, ID> {

    ENTITY findById(ID id);

    ENTITY findByIdElseThrowException(ID id);

    List<ENTITY> findAll();

    List<ENTITY> findAllElseThrowException();

    ENTITY save(ENTITY entity);

    List<ENTITY> saveAll(Iterable<ENTITY> entities);

    void delete(ENTITY entity);

    void deleteAll(Iterable<ENTITY> entities);

}
