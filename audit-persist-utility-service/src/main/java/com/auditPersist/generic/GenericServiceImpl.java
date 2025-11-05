package com.auditPersist.generic;


import com.auditPersist.exeptions.NoValidDataException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public class GenericServiceImpl<ENTITY, REPO extends JpaRepository, ID> implements GenericService<ENTITY, ID> {

    REPO entityRepository;

    public GenericServiceImpl(REPO entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public ENTITY findById(ID id) {
        if (id == null) return null;
        Optional<ENTITY> entityRecord = entityRepository.findById(id);
        if (entityRecord.isPresent())
            return entityRecord.get();
        return null;
    }

    @Override
    public ENTITY findByIdElseThrowException(ID id) {
        if (id == null)
            throw new NoValidDataException("Id is Null Please try to enter some value next time");
        Optional<ENTITY> entityRecord = entityRepository.findById(id);
        if (entityRecord.isPresent())
            return entityRecord.get();
        throw new NoValidDataException("No Recode Match With This Id ??");
    }

    @Override
    public List<ENTITY> findAll() {
        return entityRepository.findAll();
    }

    @Override
    public List<ENTITY> findAllElseThrowException() {
        List<ENTITY> entityRecords = entityRepository.findAll();
        if (entityRecords == null)
            throw new NoValidDataException("The Is No Record Of This Entity");
        return entityRecords;
    }

    @Override
    public ENTITY save(ENTITY entity) {
        return (ENTITY) entityRepository.saveAndFlush(entity);
    }

    @Override
    public List<ENTITY> saveAll(Iterable<ENTITY> entities) {
        return entityRepository.saveAllAndFlush(entities);
    }

    @Override
    public void delete(ENTITY entity) {
        try {
            entityRepository.delete(entity);
        } catch (Exception e) {
            throw new NoValidDataException("This Record Cant Be Deleted Contact Admin For More Information");
        }
    }

    @Override
    public void deleteAll(Iterable<ENTITY> entities) {
        try {
            entityRepository.deleteAll(entities);
        } catch (Exception e) {
            throw new NoValidDataException("This Records Cant Be Deleted Contact Admin For More Information");
        }
    }

    public REPO getEntityRepository() {
        return entityRepository;
    }

}
